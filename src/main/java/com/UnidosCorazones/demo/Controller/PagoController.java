package com.UnidosCorazones.demo.Controller;

import com.UnidosCorazones.demo.Service.DonacionService;
import com.paypal.core.PayPalHttpClient;
import com.paypal.orders.*;
import com.paypal.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pagos")
public class PagoController {

    @Autowired
    private PayPalHttpClient payPalHttpClient;

    @Autowired
    private DonacionService donacionService;

    @PostMapping("/crear")
    public ResponseEntity<Map<String, String>> crearOrden(@RequestBody Map<String, String> datos) {
        try {
            OrderRequest orderRequest = new OrderRequest();
            orderRequest.checkoutPaymentIntent("CAPTURE");

            List<PurchaseUnitRequest> purchaseUnits = new ArrayList<>();
            PurchaseUnitRequest purchaseUnitRequest = new PurchaseUnitRequest()
                    .amountWithBreakdown(new AmountWithBreakdown()
                            .currencyCode("USD")
                            .value(datos.get("monto")));

            purchaseUnits.add(purchaseUnitRequest);
            orderRequest.purchaseUnits(purchaseUnits);

            OrdersCreateRequest request = new OrdersCreateRequest().requestBody(orderRequest);
            HttpResponse<Order> response = payPalHttpClient.execute(request);

            // Devolvemos un JSON válido explícitamente
            return ResponseEntity.ok(Collections.singletonMap("id", response.result().id()));

        } catch (IOException e) {
            e.printStackTrace(); // Mira la consola de Java si falla aquí
            return ResponseEntity.internalServerError().body(Collections.singletonMap("error", "Error interno"));
        }
    }

    @PostMapping(value = "/capturar/{orderId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> capturarPago(
            @PathVariable String orderId,
            @RequestBody Map<String, Object> datosFormulario // <--- RECIBIMOS DATOS DEL FORM
    ) {
        try {
            // A. Ejecutar cobro en PayPal
            OrdersCaptureRequest request = new OrdersCaptureRequest(orderId);
            request.requestBody(new OrderRequest());
            HttpResponse<Order> response = payPalHttpClient.execute(request);

            // B. Si PayPal dice "COMPLETED", guardamos en nuestra BD
            if ("COMPLETED".equals(response.result().status())) {

                String nombre = (String) datosFormulario.get("nombre");
                String email = (String) datosFormulario.get("email");
                // Convertir monto de String/Double a BigDecimal
                BigDecimal monto = new BigDecimal(String.valueOf(datosFormulario.get("monto")));

                // Obtener usuario logueado desde Spring Security
                String usuarioLogueado = SecurityContextHolder.getContext().getAuthentication().getName();

                // C. GUARDAR
                donacionService.registrarDonacion(nombre, email, monto, usuarioLogueado);
            }

            return ResponseEntity.ok(Map.of(
                    "status", response.result().status(),
                    "id", response.result().id()
            ));

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Collections.singletonMap("error", "Error al procesar pago"));
        }
    }
}