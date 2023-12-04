package com.onlinemobilestore.API;

import com.onlinemobilestore.dto.OrderForUserDTO;
import com.onlinemobilestore.dto.ProductInOrderDTO;
import com.onlinemobilestore.entity.*;

import com.onlinemobilestore.repository.*;
import com.onlinemobilestore.service.serviceImpl.DiscountServiceImpl;
import com.onlinemobilestore.service.serviceImpl.OrderServiceImpl;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
@CrossOrigin
public class UserAdminRestController {
    @Autowired
    ProductRepository proDAO;
    @Autowired
    DiscountServiceImpl discountService;
    @Autowired
    private OrderServiceImpl orderService;
//    @Autowired
//    private AuthenticationManager authenticationManager;trademarkRepository
@Autowired
private TrademarkRepository trademarkRepository;
    @Autowired
    private OrderRepository orderRepositoryz;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
@Autowired
private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    UserRepository userDao;
    @Autowired
    CategoryRepository categoryDAO;
    @Autowired
    ProductRepository productDAO;
    @Autowired
    DiscountRepository discountDAO;
    @Autowired
    private AuthenticationManager authenticationManager;


    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/getProducts/{nameProduct}/{ramId}")
    public List<Information> getProductsByNameAndRamId(@PathVariable("nameProduct") String nameProduct,
                                                       @PathVariable("ramId") Integer ramId) {
        List<Product> products = proDAO.findByName(nameProduct);
        List<Color> colors = products.stream().map(Product::getColor).collect(Collectors.toList());
        List<Storage> storages = products.stream().map(Product::getStorage).collect(Collectors.toList());
        List<Information> result = new ArrayList<>();

        for (Product product : products) {
            if (product.getStorage().getId() == ramId) {
                DataProductDetail productDetail = new DataProductDetail(
                        product.getId(),
                        product.getName(),
                        product.getPrice() * (1 - (product.getPercentDiscount() / 100)),
                        product.getPrice(),
                        product.getDiscounts(),
                        product.getStorage(),
                        product.getColor(),
                        product.getImages(),
                        product.getPreviews().stream().mapToDouble(Preview::getRate).average().orElse(0.0)
                );

                List<DataComment> dataComments = product.getPreviews().stream()
                        .map(preview -> new DataComment(
                                preview.getContent(),
                                preview.getUser().getFullName(),
                                preview.getUser().getAvatar(),
                                preview.getUser().getId(),
                                preview.getRate(),
                                preview.getCreateDate(),
                                preview.getRepPreviews().stream()
                                        .map(repPreview -> new DataRepComment(
                                                repPreview.getId(),
                                                repPreview.getCreateDate(),
                                                repPreview.getContent(),
                                                repPreview.getAdmin().getFullName()
                                        ))
                                        .collect(Collectors.toList())
                        ))
                        .collect(Collectors.toList());


                result.add(new Information(
                        colors,
                        storages,
                        productDetail,
                        dataComments
                ));
            }
        }

        return result;
    }

    @GetMapping("/getProductByRam/{ram}")
    public List<ProductByRam> getInformationProduct(@PathVariable("ram") Integer ram) {
        List<Product> products = productRepository.findByStorage_ReadOnlyMemoryValue(ram);
        List<ProductByRam> result = products.stream().map(data -> {
            List<String> imageList = Optional.ofNullable(data.getImages())
                    .orElse(Collections.emptyList())
                    .stream()
                    .map(Image::getImageUrl)
                    .collect(Collectors.toList());
            List<DiscountProduct> discounts = Optional.ofNullable(data.getDiscounts())
                    .orElse(Collections.emptyList())
                    .stream()
                    .map(discount -> new DiscountProduct(discount.getId(), discount.getName(), discount.getPercent(), discount.getExpirationDate()))
                    .collect(Collectors.toList());
            double priceOld = data.getPrice() * (100 - Optional.ofNullable(data.getPercentDiscount()).orElse(0));
            double rate = Optional.ofNullable(data.getPreviews())
                    .orElse(Collections.emptyList())
                    .stream()
                    .mapToDouble(Preview::getRate)
                    .average()
                    .orElse(0.0);
            ProductDetail productDetail = new ProductDetail(data.getId(), data.getName(), data.getColor().getColor(), data.getPrice(), priceOld, data.getStorage(), rate);
            List<Comment> commentList = Optional.ofNullable(data.getPreviews())
                    .orElse(Collections.emptyList())
                    .stream()
                    .map(preview -> {
                        List<RepComment> repCommentList = Optional.ofNullable(preview.getRepPreviews())
                                .orElse(Collections.emptyList())
                                .stream()
                                .map(repPreview -> new RepComment(
                                        repPreview.getId(),
                                        repPreview.getCreateDate(),
                                        repPreview.getContent(),
                                        repPreview.getAdmin() != null ? repPreview.getAdmin().getFullName() : ""))
                                .collect(Collectors.toList());

                        return new Comment(
                                preview.getId(),
                                preview.getCreateDate(),
                                preview.getRate(),
                                preview.getContent(),
                                preview.getUser() != null ? preview.getUser().getFullName() : "",
                                repCommentList);
                    })
                    .collect(Collectors.toList());

            return new ProductByRam(data.getId(), imageList, discounts, productDetail, commentList);
        }).collect(Collectors.toList());

        return result;
    }
    @GetMapping("/account")
    public List<UserData> getAllAccount() {
        System.out.println("In thử trong api khác nè");
        List<User> data = userDao.findAll();
        List<UserData> userDataList = new ArrayList<UserData>();
        for (User user : data) {
//            List<RoleAssignment> roleAssignments = user.getRoleAssignments();
//            List<String> roleNames = new ArrayList<>();
//            for (RoleAssignment roleAssignment : roleAssignments) {
//                roleNames.add(roleAssignment.getRole().getName());
//            }
            userDataList.add(new UserData(user.getId(), user.getEmail(), user.getAddress(), user.getFullName(),
                    user.getAvatar(), user.getBirthday(), user.getPhoneNumber(), user.getCreateDate(),user.isActive(), user.getRoles()));
        }
        return userDataList;
    }

    @DeleteMapping("/account/delete/{id}")
    public dataUser deleteUser(@PathVariable Integer id) {
        Optional<User> userOptional = userDao.findById(id);
        if (userOptional.isEmpty()) {
            List<User> data = userDao.findAll();
            List<UserData> userDataList = new ArrayList<>();
            for (User user : data) {

                userDataList.add(new UserData(user.getId(), user.getEmail(), user.getAddress(), user.getFullName(),
                        user.getAvatar(), user.getBirthday(), user.getPhoneNumber(), user.getCreateDate(),user.isActive(), user.getRoles()));
            }
            return new dataUser("User not found", userDataList);
        }

        userDao.deleteById(id);

        List<User> data = userDao.findAll();
        List<UserData> userDataList = new ArrayList<>();
        for (User user : data) {

            userDataList.add(new UserData(user.getId(), user.getEmail(), user.getAddress(), user.getFullName(),
                    user.getAvatar(), user.getBirthday(), user.getPhoneNumber(), user.getCreateDate(),user.isActive(), user.getRoles()));
        }

        String message = "User with id " + id + " has been deleted";
        return new dataUser(message, userDataList);
    }

    @PutMapping("/account/{id}/activate")
    public dataUser activateUser(@PathVariable Integer id) {
        Optional<User> userOptional = userDao.findById(id);
        if (userOptional.isEmpty()) {
            List<User> data = userDao.findAll();
            List<UserData> userDataList = new ArrayList<>();
            for (User user : data) {

                userDataList.add(new UserData(user.getId(), user.getEmail(), user.getAddress(), user.getFullName(),
                        user.getAvatar(), user.getBirthday(), user.getPhoneNumber(), user.getCreateDate(),user.isActive(), user.getRoles()));
            }
            return new dataUser("User not found", userDataList);
        }

        User users = userOptional.get();
        users.setActive(true);
        userDao.save(users);

        List<User> data = userDao.findAll();
        List<UserData> userDataList = new ArrayList<>();
        for (User user : data) {

            userDataList.add(new UserData(user.getId(), user.getEmail(), user.getAddress(), user.getFullName(),
                    user.getAvatar(), user.getBirthday(), user.getPhoneNumber(), user.getCreateDate(),user.isActive(), user.getRoles()));
        }

        String message = "User with id " + id + " has been activated";
        return new dataUser(message, userDataList);
    }

    @PutMapping("/account/{id}/deactivate")
    public dataUser deactivateUser(@PathVariable Integer id) {
        Optional<User> userOptional = userDao.findById(id);
        if (userOptional.isEmpty()) {
            List<User> data = userDao.findAll();
            List<UserData> userDataList = new ArrayList<>();
            for (User user : data) {

                userDataList.add(new UserData(user.getId(), user.getEmail(), user.getAddress(), user.getFullName(),
                        user.getAvatar(), user.getBirthday(), user.getPhoneNumber(), user.getCreateDate(),user.isActive(), user.getRoles()));
            }
            return new dataUser("User not found", userDataList);
        }

        User users = userOptional.get();
        users.setActive(false);
        userDao.save(users);

        List<User> data = userDao.findAll();
        List<UserData> userDataList = new ArrayList<>();
        for (User user : data) {

            userDataList.add(new UserData(user.getId(), user.getEmail(), user.getAddress(), user.getFullName(),
                    user.getAvatar(), user.getBirthday(), user.getPhoneNumber(), user.getCreateDate(),user.isActive(), user.getRoles()));
        }

        String message = "User with id " + id + " has been deactivated";
        return new dataUser(message, userDataList);
    }


//    Begin
    @GetMapping("/getTypePhone")
    public List<String> getTypePhone() {
        return categoryDAO.getAllName();
    }

//    @GetMapping("/getProducts")
//    public List<DataProduct> getAllProduct() {
//        List<Product> data = productDAO.findAll();
//        List<DataProduct> jsonData = new ArrayList<>();
//        for (Product pro : data) {
//            jsonData.add(new DataProduct(pro.getId(), pro.getName(), pro.getPrice(),pro.getPrice()*(100-pro.getPercentDiscount())));
//        }
//        return jsonData;
//    }



    @GetMapping("/getProductsDiscount")
    public List<DataProductHasDiscount> getAllProductDiscount() {
        List<Product> data = productDAO.findAll();
        List<DataProductHasDiscount> jsonData = new ArrayList<>();
        DiscountBrief maxPrecent;
        for (Product pro : data) {
            List<Discount> listDiscount = pro.getDiscounts();
            System.out.println(data.size());
            System.out.println("ffjfj");
            if (listDiscount.size() > 0){
                List<DiscountBrief> discountBriefs = new ArrayList<>();
                listDiscount.stream().forEach(dis -> discountBriefs.add(new DiscountBrief(dis.getName(), dis.getPercent(), dis.getExpirationDate(), dis.getCreateDate(), dis.isActive())));
                maxPrecent = discountBriefs.get(0);
                for(int i = 0; i < discountBriefs.size(); i++){
                    if(discountBriefs.get(i).getPercent() > maxPrecent.getPercent()){
                        maxPrecent = discountBriefs.get(i);
                    }
                }
                jsonData.add(new DataProductHasDiscount(pro.getId(), pro.getImages() , pro.getName(), pro.getPrice(),
                        Math.floor(pro.getPrice() - (pro.getPrice()*pro.getPercentDiscount()/100)), pro.getPercentDiscount(), discountBriefs));
            }

        }
        return jsonData;
    }

    @GetMapping("/getDiscounts")
    public List<Discount> getDiscounts() {
        List<Discount> data = discountDAO.findAll();
        return data;
    }

    @GetMapping("/getProducts/{name}")
    public List<DataProductHasDiscount> getProductByName(@PathVariable("name") String name) {
        List<Product> data = productDAO.findByName(name);

        List<DataProductHasDiscount> jsonData = new ArrayList<>();
        for (Product pro : data) {
            List<DiscountBrief> discountBriefs = new ArrayList<>();
            pro.getDiscounts().stream().forEach(dis -> discountBriefs.add(new DiscountBrief(dis.getName(), dis.getPercent(), dis.getExpirationDate(), dis.getCreateDate(), dis.isActive())));
            jsonData.add(new DataProductHasDiscount(pro.getId(), pro.getImages(), pro.getName(),
                    pro.getPrice(), (pro.getPrice() - pro.getPrice()*(pro.getPercentDiscount()/100)), pro.getPercentDiscount(), discountBriefs));
        }
        return jsonData;
    }


    @CrossOrigin("*")
    @PostMapping("/signin/{email}/{password}")
    public User authenticateUser(@PathVariable("email") String email, @PathVariable("password") String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email.trim(), password.trim()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            User user = userRepository.findUserByEmail(email);
            return user;
        } catch (AuthenticationException e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/getInformationBrand/{id}")
    public List<InformationBrand> getInformationBrand(@PathVariable("id") int id) {
        List<Product> productList = productRepository.findByTrademarkId(id);
        List<Product> productListStore = new ArrayList<Product>();

        List<InformationBrand> informationBrandList = new ArrayList<>();
        float maxPercent = 0;
        for (Product data : productList) {
            List<Storage> storages = new ArrayList<Storage>();
            productListStore = data.getCategory().getProducts();
            productListStore.stream().forEach(productDetail -> {
                storages.add(productDetail.getStorage());
            });
            if(data.getDiscounts().size() > 0){
                maxPercent = data.getDiscounts().get(0).getPercent();
            }
            InformationBrand informationBrand = new InformationBrand(
                    data.getId(),
                    data.getName(),
                    data.getPrice(),
                    Math.floor((data.getPrice() * (100 - data.getPercentDiscount()) / 100) ),
                    data.getQuantity(),
                    data.getDescription(),
                    data.getImages(),
                    data.getCreateDate(),
                    data.getModifiedDate(),
                    data.getPercentDiscount(),
                    storages
            );
            informationBrandList.add(informationBrand);
        }
        return informationBrandList; // Trả về danh sách InformationBrand
    }

    @GetMapping("/getOrder/{id}")
    public List<OrderDetailz> getOrder(@PathVariable("id") Integer id){
        try {
            List<OrderDetail> orderz = orderDetailRepository.findAllByOrderId(id);
            List<OrderDetailz> orderDetail = new ArrayList<OrderDetailz>();
            for(OrderDetail od : orderz){
                orderDetail.add(new OrderDetailz(od.getId(), od.getOrder().isState(),
                         od.getProduct().getName(),od.getProduct().getImages(),
                        od.getPrice(), od.getQuantity(), od.getCreateDate(), od.getProduct().getDiscounts(), od.getProduct().getId()));
            }
            return orderDetail;
        } catch(Exception e) {
            return null;
        }
    }

        @GetMapping ("/getOrderNew/{userId}/{productId}")
    public ProductInOrderDTO getOrderNew(@PathVariable("userId") int userId ,
                                         @PathVariable("productId") int productId){
       return orderService.createOrderAndOrderDetailByUserIdAndByProductId(userId, productId);
    }

    @GetMapping("/getOrders/{userId}")
    public List<OrderForUserDTO> getOrdersForUser(@PathVariable("userId") int userId){
        return orderService.getOrdersForUser(userId);
    }

    @GetMapping("/deleteOrder/{idOrder}/{idUser}")
    public List<OrderForUserDTO> deleteOrder(@PathVariable Integer idOrder,
                                          @PathVariable Integer idUser) {
        List<OrderForUserDTO> orderList = new ArrayList<>();
        Order orderToDelete = orderRepositoryz.findByIdAndUserId(idOrder, idUser);
        // Kiểm tra xem Order có tồn tại không
        if (orderToDelete != null) {
            // Nếu tồn tại, xóa bằng cách sử dụng hàm delete
            orderRepositoryz.delete(orderToDelete);
            // Tạo danh sách mới sau khi xóa
            return orderService.getOrdersForUser(idUser);
        } else {
            // Nếu không tồn tại, trả về danh sách trống
            return  null;
        }
    }

    @GetMapping("/deleteOrderDetail/{idOrderDetail}/{idOrder}")
    public List<OrderDetailz> deleteOrderDetail(
            @PathVariable Integer idOrderDetail,
            @PathVariable Integer idOrder) {

        List<OrderDetailz> orderDetailList = new ArrayList<>();

        // Kiểm tra xem OrderDetail có tồn tại không
        if (orderDetailRepository.existsById(idOrderDetail)) {
            // Nếu tồn tại, xóa bằng cách sử dụng hàm deleteById
            orderDetailRepository.deleteById(idOrderDetail);

            // Tạo danh sách mới sau khi xóa
            for (OrderDetail od : orderDetailRepository.findAllByOrderId(idOrder)) {

                orderDetailList.add(new OrderDetailz(od.getId(),od.getOrder().isState(),
                        od.getProduct().getName(), od.getProduct().getImages(),
                        od.getPrice(), od.getQuantity(), od.getCreateDate(), od.getProduct().getDiscounts(), od.getProduct().getId()));
            }
        } else {
            return orderDetailList;
        }
        return orderDetailList;
    }

    @GetMapping("/api/discount/{name}/{orderId}")
    public ResponseEntity<List<OrderDetail>> getDiscount(@PathVariable("name") String name,
                                                         @PathVariable("orderId") int orderId) {
        Discount discount = discountService.findDiscountByName(name);
        if (discount == null) {
            return ResponseEntity.notFound().build();
        }
        List<OrderDetail> orderDetails = orderDetailRepository.findAllByOrderId(orderId);
        orderDetails.forEach(orderDetail -> {
            if (orderDetail.getProduct().getId() == discount.getProduct().getId()) {
                double discountedPrice = orderDetail.getPrice() * (1 - (discount.getPercent() / 100.0));
                orderDetail.setPrice(discountedPrice);
                orderDetailRepository.save(orderDetail);
            }
        });
        return ResponseEntity.ok(orderDetails);
    }



    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class ProductByRam {
        private Integer id;
        private List<String> imageList;
        private List<DiscountProduct> discountProducts;
        private ProductDetail productDetail;
        private List<Comment> commentList;
    }
    @Data
    @AllArgsConstructor
    class DiscountProduct {
        private Integer id;
        private String name;
        private double percent;
        private Date expirationDate;
    }

    @Data
    @AllArgsConstructor
    class ProductDetail {
        private int id;
        private String name;
        private String color;
        private double priceNew;
        private double priceOld;
        private Storage storage;
        private double rate;
    }

    @Data
    @AllArgsConstructor
    class Comment {
        private int id;
        private Date createDate;
        private double rate;
        private String content;
        private String userName;
        private List<RepComment> repCommentList;
    }

    @Data
    @AllArgsConstructor
    class RepComment {
        private int id;
        private Date createDate;
        private String content;
        private String adminName;
    }

    @Data
    @AllArgsConstructor
    class Information {
        private List<Color> colors;
        private List<Storage> storages;
        private DataProductDetail dataProductDetail;
        private List<DataComment> commentList;
    }
        @Data
    @AllArgsConstructor
    class OrderForUser{
        private int id;
        private Double total;
        private int quantity;
        private boolean state;
        private int code;
        private Date createDate;

    }
    @Data
    @AllArgsConstructor
    class OrderDetailz{
        private int id;
        private boolean state;
        private String name;
        private List<Image> images;
        private double price;
        private int quantity;
        private Date createDate;
        private List<Discount> discounts;
        private int idProduct;

    }





    @Data
    @AllArgsConstructor
    class InformationBrand{
        private int id;
        private String name;
        private double price;
        private double priceUpdate;
        private int quantity;
        private String description;
        private List<Image> images;
        private Date createDate;
        private Date modifiedDate;
        private float percentDiscount;
        private List<Storage> storages;

    }


    @Data
    @AllArgsConstructor
    class DataProductHasDiscount {
        private Integer id;
        private List<Image> images;
        private String name;
        private Double price;
        private Double priceUpdate;
        private int percentDiscount;
        private List<DiscountBrief> discount;
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    class DiscountBrief {
        private String name;
        private float percent;
        private Date expiration_date;
        private Date create_date;
        private boolean active;
    }



//    End








    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    class UserData {
        private Integer id;
        private String email;
        private String address;
        private String fullName;
        private String avatar;
        private Date birthday;
        private String phoneNumber;
        private Date createDate;
        private boolean isActive;
        private String roles;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    class dataUser {
        private String message;
        private List<UserData> userDataList;
    }

}