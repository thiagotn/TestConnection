package br.com.uol.produtos.testconnection

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import br.com.uol.produtos.testconnection.server.*
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.MalformedURLException
import java.net.URL


class MainActivity : AppCompatActivity() {

    companion object {
        lateinit var dialog: SpotsDialog
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dialog = SpotsDialog(this, "Carregando...")

        btn_test.onClick {
            val target = et_url.text.toString()
            if (isValidateUrl(target)) {
                createRequest(target)
            } else {
                val errorMsg = "Endereço inválido."
                txt_result.text = errorMsg
                toast(errorMsg)
            }
        }
    }

    private fun isValidateUrl(target: String): Boolean {
        var valid = true
        try {
            URL(target)
        } catch (e: MalformedURLException) {
            valid = false
        }
        return valid
    }

    private fun createRequest(targetUrl: String) {
        dialog.show()

        Log.i(this::class.simpleName, "[1] Async...")
        val https = targetUrl.startsWith("https", true)

        BaseRequest(targetUrl, https).retrofit().create(TestService::class.java).test().enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                val r = response.raw()
                val resultText = "Response:\n Code: ${r.code()}\n Message: ${r.message()}\n Raw: ${response.raw()}"
                showResponse(resultText)
                Log.i(this::class.simpleName, "post submitted to API." + response.body().toString())
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {

                if (call.isCanceled()) {
                    Log.e(this::class.simpleName, "Request was aborted")
                } else {
                    Log.e(this::class.simpleName, "Unable to submit post to API.")
                }
                showResponse("Cause: ${t.cause}\n Message: ${t.message}\n StackTrace: ${t.stackTrace}")
            }
        })
    }

    private fun showResponse(resultText: String) {
        Log.i(this::class.simpleName, "showResult: ${resultText}")
        dialog.dismiss()
        txt_result.text = resultText
    }
}
