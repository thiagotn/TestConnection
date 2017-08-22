package br.com.uol.produtos.testconnection.server

import retrofit2.Call
import retrofit2.http.GET

/**
 * Created by tnogueira on 21/08/17.
 */
interface TestService {

    @GET("/") fun test(): Call<Void>

    @GET("/points") fun points(): Call<Void>
}