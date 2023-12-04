package com.onlinemobilestore.API;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlinemobilestore.entity.*;
import com.onlinemobilestore.repository.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/admin")
public class ProductAPI {
//    Begin test

//    End Test

    @Autowired
    ProductRepository proDAO;
    @Autowired
    CategoryRepository cateDAO;
    @Autowired
    ColorRepository colDAO;
    @Autowired
    StorageRepository stoDAO;
    @Autowired
    TrademarkRepository traDAO;
    @Autowired
    ImageRepository imgDAO;
    // API viết cho Tình
    @GetMapping("/getProducts")
    public List<DataProduct> getAllProduct() {
        List<Product> data = proDAO.findAll();
        List<DataProduct> jsonData = new ArrayList<>();
        for (Product pro : data) {
            jsonData.add(new DataProduct(pro.getId(), pro.getName(),
                    pro.getPrice(), pro.getPrice() * (100 - pro.getPercentDiscount())/100, pro.getPercentDiscount()));
        }
        System.out.println("test");
        return jsonData;
    }

    @GetMapping("/getInformation/{id}")
    public Information getInformationProduct(@PathVariable("id") Integer id) {
        Product data = proDAO.findById(id).get();
        List<Product> productList = data.getCategory().getProducts();
        System.out.println(productList.size());
        List<Color> colorList = new ArrayList<>();
        List<Storage> storageList = new ArrayList<>();
        List<DataComment> commentList = new ArrayList<>();
        data.getPreviews().stream().forEach(preview -> {
            List<DataRepComment> repCommentList = new ArrayList<>();
            preview.getRepPreviews().stream().forEach(repPreview -> repCommentList.add(new DataRepComment(repPreview.getId(), repPreview.getCreateDate(), repPreview.getContent(), repPreview.getAdmin().getFullName())));
            commentList.add(new DataComment(preview.getContent(), preview.getUser().getFullName(), preview.getUser().getAvatar(), preview.getUser().getId(), preview.getRate(), preview.getCreateDate(), repCommentList));
        });
        productList.stream().forEach(productDetail -> {
            colorList.add(productDetail.getColor());
            storageList.add(productDetail.getStorage());
        });
        Double rate = commentList.stream().mapToDouble(DataComment::getRate)
                .average().orElse(0);
        DataProductDetail dataProductDetail = new DataProductDetail(data.getId(), data.getName(), data.getPrice() * (100 - data.getPercentDiscount()), data.getPrice(), data.getDiscounts(), data.getStorage(), data.getColor(), data.getImages(), rate);
        return new Information(colorList, storageList, dataProductDetail, commentList);
    }


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
class DataProductDetail {
    private Integer id;
    private String name;
    private Double priceNew;
    private Double priceOld;
    private List<Discount> discounts;
    private Storage storage;
    private Color color;
    private List<Image> images;
    private Double rate;
}

@Data
@AllArgsConstructor
class DataComment {
    private String content;
    private String nameUser;
    private String avatar;
    private Integer idUser;
    private Double rate;
    private Date createDate;
    private List<DataRepComment> repPreviews;
}

@Data
@AllArgsConstructor
class DataRepComment {
    private int id;
    private Date createDate;
    private String content;
    private String fullName;
}


@Data
@AllArgsConstructor
class DataProduct {
    private Integer id;
    private String name;
    private Double price;
    private Double priceUpdate;
    private float personDiscount;
}
