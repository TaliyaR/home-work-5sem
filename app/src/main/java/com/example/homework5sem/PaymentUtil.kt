package com.example.homework5sem

import android.app.Activity
import com.google.android.gms.wallet.PaymentsClient
import com.google.android.gms.wallet.Wallet
import com.google.android.gms.wallet.WalletConstants
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


object PaymentUtil {

    //Экземпляр PaymentsClient используется для взаимодействия с Google Pay API
    fun createPaymentsClient(activity: Activity): PaymentsClient {
        val walletOptions = Wallet.WalletOptions.Builder()
            .setEnvironment(WalletConstants.ENVIRONMENT_TEST)
            .build()
        return Wallet.getPaymentsClient(activity, walletOptions)
    }

    //Объект базового запроса Google Pay API со свойствами,
    //используемыми во всех запросах.
    private fun baseRequest(): JSONObject = JSONObject()
        .put("apiVersion", 2)
        .put("apiVersionMinor", 0)

    //поддерживаемые приложением платежные системы.
    private fun allowedCardNetworks() = JSONArray(
        listOf(
            "AMEX",
            "DISCOVER",
            "INTERAC",
            "JCB",
            "MASTERCARD",
            "VISA"
        )
    )

    private fun allowedCardAuthMethods() = JSONArray(
        listOf(
            "PAN_ONLY",
            "CRYPTOGRAM_3DS"
        )
    )

    //Шифруется информация о выбранной карте плательщика
    // example – тестовое название шлюза.
    private fun gatewayTokenizationSpecification(): JSONObject {
        return JSONObject().apply {
            put("type", "PAYMENT_GATEWAY")
            put(
                "parameters", JSONObject(
                    mapOf(
                        "gateway" to "example",
                        "gatewayMerchantId" to "exampleGatewayMerchantId"
                    )
                )
            )
        }
    }

    //поддерживаемые способы оплаты
    private fun baseCardPaymentMethod(): JSONObject = JSONObject().apply {
        put("type", "CARD")
        put(
            "parameters", JSONObject(
                mapOf(
                    "allowedAuthMethods" to allowedCardAuthMethods(),
                    "allowedCardNetworks" to allowedCardNetworks()
                )
            )
        )
    }

    //описание информации, которая  должна вернуться,включая
    //токенезированные данные
    private fun cardPaymentMethod() = baseCardPaymentMethod()
        .put("tokenizationSpecification", gatewayTokenizationSpecification())

    //Определить готовность к оплате с помощью Google Pay API
    //Добавены разрешенные способы оплаты в базовый объект запроса
    fun isReadyToPayRequest(): JSONObject? {
        return try {
            baseRequest().apply {
                put("allowedPaymentMethod", JSONArray().put(baseCardPaymentMethod()))
            }
        } catch (e: JSONException) {
            null
        }
    }

    //создание информации о транзакции
    private fun getTransactionInfo(price: String) =
        JSONObject().apply {
            put("totalPrice", price)
            put("totalPriceStatus", "FINAL")
            put("countryCode", "US")
            put("currencyCode", "USD")
        }

    //Информация о продавце, запрашивающем платежную информацию
    private val merchantInfo: JSONObject =
        JSONObject().put("merchantName", "Example Merchant")

    //* Объект, описывающий информацию, запрашиваемую в листе оплаты Google Pay.
    fun getPaymentDataRequest(price: String): JSONObject? {
        try {
            return baseRequest().apply {
                put("allowedPaymentMethods", JSONArray().put(cardPaymentMethod()))
                put("transactionInfo", getTransactionInfo(price))
                put("merchantInfo", merchantInfo)
            }
        } catch (e: JSONException) {
            return null
        }
    }
}
