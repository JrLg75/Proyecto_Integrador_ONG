package com.UnidosCorazones.demo.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Mapeamos la URL "/media/**" a la carpeta física "uploads"
        // "file:uploads/" indica que busque en la raíz del proyecto
        registry.addResourceHandler("/media/**")
                .addResourceLocations("file:uploads/");
    }
}
