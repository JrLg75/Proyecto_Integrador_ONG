package com.UnidosCorazones.demo.Config; // Asegúrate que el package coincida con lo que creaste

import com.paypal.core.PayPalEnvironment;
import com.paypal.core.PayPalHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaypalConfig {

    // RECUERDA: Cambia esto por tus credenciales reales cuando puedas
    private String clientId = "AXJ0ccquxZwLahQEkaoESrNm_KStX6g307kmEjk3tDe6p4Qev0WMDzEp6pJ0wSOR0_SfHD_meE3Ogm2B";
    private String clientSecret = "EF2h5AgYOrvju1VH9gcb-aQUtCcKv4TaXzW2Erp9U3ObwHJNdt3GC0DpABthu_YsrvAlw_Dcva9_gfxw";

    @Bean
    public PayPalHttpClient payPalHttpClient() {

        PayPalEnvironment environment = new PayPalEnvironment.Sandbox(clientId, clientSecret);
        return new PayPalHttpClient(environment);
    }
}