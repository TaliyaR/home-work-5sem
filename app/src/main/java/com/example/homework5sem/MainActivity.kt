package com.example.homework5sem

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.wallet.*
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONException
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private val LOAD_PAYMENT_DATA_REQUEST_CODE = 991

    private lateinit var paymentsClient: PaymentsClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        paymentsClient = PaymentUtil.createPaymentsClient(this)
        possiblyShowGooglePayButton()
        googlePayButton.setOnClickListener { requestPayment() }

    }

    //Возможность оплатить способом оплаты, поддерживаемым приложением,
    // и отображение кнопки оплаты через Google Pay.
    private fun possiblyShowGooglePayButton() {
        val isReadyToPayJson = PaymentUtil.isReadyToPayRequest() ?: return
        val request = IsReadyToPayRequest.fromJson(isReadyToPayJson.toString()) ?: return

        val task = paymentsClient.isReadyToPay(request)
        task.addOnCompleteListener { completedTask ->
            try {
                completedTask.getResult(ApiException::class.java)?.let(::setGooglePayAvailable)
            } catch (e: ApiException) {
                Log.w("isReadyToPay failed", e)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            LOAD_PAYMENT_DATA_REQUEST_CODE -> {
                when (resultCode) {
                    RESULT_OK ->
                        data?.let { intent ->
                            PaymentData.getFromIntent(intent)?.let(::handlePaymentSuccess)
                        }
                    RESULT_CANCELED -> {

                    }
                    AutoResolveHelper.RESULT_ERROR -> {
                        AutoResolveHelper.getStatusFromIntent(data)?.let {
                            handleError(it.statusCode)
                        }
                    }
                }
                googlePayButton.isClickable = true
            }

        }
    }

    private fun setGooglePayAvailable(available: Boolean) {
        if (available) {
            googlePayButton.visibility = View.VISIBLE
        } else {
            Toast.makeText(
                this,
                "Unfortunately, Google Pay is not available on this device",
                Toast.LENGTH_LONG
            ).show();
        }
    }

    private fun requestPayment() {
        googlePayButton.isClickable = false

        val paymentDataRequestJson = PaymentUtil.getPaymentDataRequest("1.00")
        if (paymentDataRequestJson == null) {
            Log.e("RequestPayment", "Can't fetch payment data request")
            return
        }
        val request = PaymentDataRequest.fromJson(paymentDataRequestJson.toString())

        if (request != null) {
            AutoResolveHelper.resolveTask(
                paymentsClient.loadPaymentData(request), this, LOAD_PAYMENT_DATA_REQUEST_CODE
            )
        }
    }

    private fun handleError(statusCode: Int) {
        Log.w("loadPaymentData failed", String.format("Error code: %d", statusCode))
    }

    private fun handlePaymentSuccess(paymentData: PaymentData) {
        val paymentInformation = paymentData.toJson() ?: return

        try {
            val paymentMethodData =
                JSONObject(paymentInformation).getJSONObject("paymentMethodData")

            Log.d(
                "GooglePaymentToken", paymentMethodData
                    .getJSONObject("tokenizationData")
                    .getString("token")
            )

        } catch (e: JSONException) {
            Log.e("handlePaymentSuccess", "Error: " + e.toString())
        }

    }
}