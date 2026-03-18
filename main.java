package com.example.productsearch;

import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired; import org.springframework.data.jpa.repository.*;
import org.springframework.web.bind.annotation.*; import org.springframework.stereotype.*;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*; import java.util.List;

/* -------------------- ENTITY	*/

@Entity
class Product {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
 
private String name; private String category; private double price;

public Product() {}

public Product(String name,String category,double price){ this.name=name;
this.category=category; this.price=price;
}

public Long getId(){ return id; }
public String getName(){ return name; } public String getCategory(){ return category; } public double getPrice(){ return price; }

public void setName(String name){ this.name=name; }
public void setCategory(String category){ this.category=category; } public void setPrice(double price){ this.price=price; }
}

/* -------------------- REPOSITORY	*/

interface ProductRepository extends JpaRepository<Product,Long>{

// Derived Query Methods
List<Product> findByCategory(String category); List<Product> findByPriceBetween(double min,double max);
// JPQL Queries
@Query("SELECT p FROM Product p ORDER BY p.price") List<Product> sortByPrice();

@Query("SELECT p FROM Product p WHERE p.price > :price") List<Product> findExpensive(@Param("price") double price);

@Query("SELECT p FROM Product p WHERE p.category = :category") List<Product> getByCategory(@Param("category") String category);
}

/* -------------------- CONTROLLER	*/

@RestController @RequestMapping("/products") class ProductController {

@Autowired ProductRepository repo;
 
@GetMapping("/category/{category}")
public List<Product> getByCategory(@PathVariable String category){ return repo.findByCategory(category);
}

@GetMapping("/filter")
public List<Product> filter(@RequestParam double min, @RequestParam double max){
return repo.findByPriceBetween(min,max);
}

@GetMapping("/sorted") public List<Product> sorted(){
return repo.sortByPrice();
}

@GetMapping("/expensive/{price}")
public List<Product> expensive(@PathVariable double price){ return repo.findExpensive(price);
}
}

@SpringBootApplication
public class ProductSearchApplication {

public static void main(String[] args) { SpringApplication.run(ProductSearchApplication.class,args);
}
}
