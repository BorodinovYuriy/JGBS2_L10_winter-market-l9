package ru.gb.wintermarket.carts.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ru.gb.wintermarket.api.dto.ProductDto;
import ru.gb.wintermarket.api.exceptions.ResourceNotFoundException;
import ru.gb.wintermarket.carts.integrations.ProductServiceIntegration;
import ru.gb.wintermarket.carts.model.Cart;
import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartService {
    private final ProductServiceIntegration productServiceIntegration;

    @Value(("${cart-service.cart-prefix}"))//Пример "winter_cart_fjsres321HJfvsvtnsriUVvi"
    private  String cartPrefix;
    private Map<String, Cart> carts;

    @PostConstruct
    public void init() {
        carts = new HashMap <>();
    }

    public Cart getCurrentCart(String uuid) {
        String targetUuid = cartPrefix + uuid;
        if(!carts.containsKey(targetUuid)){
            carts.put(targetUuid, new Cart());
        }
        return carts.get(targetUuid);
    }

    public void add(String uuid, Long productId){
        ProductDto productDto = productServiceIntegration.
                getProductById(productId);
        getCurrentCart(uuid).add(productDto);
        carts.add(productDto);
    }

    public void removeProductById(String uuid, Long id) {
        ProductDto productDto = productServiceIntegration.getProductById(id);
        getCurrentCart(uuid).remove(productDto);

    }

    public void clear(String uuid) {
        getCurrentCart(uuid).clear();
    }


//----методы quantity------------------------
    public void increaseProductInCart(String uuid, Long id) {
        ProductDto productDto = productServiceIntegration.getProductById(id);
        getCurrentCart(uuid).increaseProduct(productDto);
    }

    public void decreaseProductInCart(String uuid, Long id) {
        ProductDto productDto = productServiceIntegration.getProductById(id);
        getCurrentCart(uuid).decreaseProduct(productDto);
    }
}
