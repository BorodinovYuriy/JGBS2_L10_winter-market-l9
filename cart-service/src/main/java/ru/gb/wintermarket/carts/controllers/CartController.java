package ru.gb.wintermarket.carts.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.gb.wintermarket.api.dto.CartDto;
import ru.gb.wintermarket.api.dto.StringResponse;
import ru.gb.wintermarket.carts.converters.CartConverter;
import ru.gb.wintermarket.carts.model.Cart;
import ru.gb.wintermarket.carts.services.CartService;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cart")
//@CrossOrigin("*")
public class CartController {
    private final CartService cartService;
    private final CartConverter cartConverter;

    @GetMapping("/generate_uuid")
    public StringResponse generateUuid() {
        return new StringResponse( UUID.randomUUID().toString() );
    }
    @GetMapping("/{uuid}/add/{id}")
    public void addToCart(@RequestHeader(name = "username", required = false)String username ,
                          @PathVariable String uuid,
                          @PathVariable Long id){
        String targetUuid = getCartUuid(username, uuid);
        cartService.add(targetUuid, id);
    }
    @GetMapping("/{uuid}/clear")
    public void clearCart(@RequestHeader(name = "username", required = false)String username ,
                          @PathVariable String uuid){
        String targetUuid = getCartUuid(username, uuid);
        cartService.clear(targetUuid);
    }
    @GetMapping("/{uuid}/remove/{id}")
    public void removeProductById(@RequestHeader(name = "username", required = false)String username ,
                                  @PathVariable String uuid,
                                  @PathVariable Long id){
        String targetUuid = getCartUuid(username, uuid);
        cartService.removeProductById(targetUuid, id);

    }
    @GetMapping("/{uuid}")
    public CartDto getCurrentCart(@RequestHeader(name = "username", required = false)String username ,
                                  @PathVariable String uuid){
        String targetUuid = getCartUuid(username, uuid);
        return cartConverter.entityToDto(cartService.getCurrentCart(targetUuid));
    }
//----------------------------------------------------------------
    @GetMapping("/{uuid}/increase/{id}")
    public void increaseProductInCart(@RequestHeader(name = "username", required = false)String username ,
                                      @PathVariable String uuid,
                                      @PathVariable Long id){
        String targetUuid = getCartUuid(username, uuid);
        cartService.increaseProductInCart(targetUuid, id);
    }
    @GetMapping("/{uuid}/decrease/{id}")
    public void decreaseProductInCart(@RequestHeader(name = "username", required = false)String username ,
                                      @PathVariable String uuid,
                                      @PathVariable Long id){
        String targetUuid = getCartUuid(username, uuid);
        cartService.decreaseProductInCart(targetUuid, id);
    }
//****************************************
    private String getCartUuid(String username, String uuid){
        if(username != null){
            return username;
        }
        return uuid;
    }

}

