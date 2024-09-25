package com.ayush.data.network

import com.ayush.data.model.AuthRequest
import com.ayush.data.model.AuthResponse
import com.ayush.data.model.DataProductModel
import com.ayush.data.model.ProductsResponse
import com.ayush.data.model.toUser
import com.ayush.domain.model.Product
import com.ayush.domain.model.User
import com.ayush.domain.network.NetworkService
import com.ayush.domain.network.ResultWrapper
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.header
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.Parameters
import io.ktor.http.contentType
import io.ktor.util.InternalAPI
import io.ktor.utils.io.errors.IOException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class NetworkServiceImpl(val client: HttpClient) : NetworkService {
    private val baseUrl = "https://diasconnect-seller.onrender.com"
    override suspend fun getProducts(): ResultWrapper<List<Product>> {
        return makeWebRequest(
            url = "$baseUrl/product/seller/6",
            method = HttpMethod.Get,
            mapper = { response: ProductsResponse ->
                response.products.map { it.toProduct() }
            }
        )
    }


    override suspend fun login(email: String, password: String): ResultWrapper<User> {
        val authRequest = AuthRequest(email = email, password = password)
        return makeWebRequest(
            url = "$baseUrl/login",
            method = HttpMethod.Post,
            body = Json.encodeToString(authRequest)
        ) { response: AuthResponse ->
            response.data?.toUser() ?: throw Exception(response.errorMessage ?: "Unknown error")
        }
    }

    override suspend fun signup(name: String, email: String, password: String): ResultWrapper<User> {
        val authRequest = AuthRequest(name = name, email = email, password = password)
        return makeWebRequest(
            url = "$baseUrl/signup",
            method = HttpMethod.Post,
            body = Json.encodeToString(authRequest)
        ) { response: AuthResponse ->
            response.data?.toUser() ?: throw Exception(response.errorMessage ?: "Unknown error")
        }
    }

    @OptIn(InternalAPI::class)
    suspend inline fun <reified T, R> makeWebRequest(
        url: String,
        method: HttpMethod,
        body: Any? = null,
        headers: Map<String, String> = emptyMap(),
        parameters: Map<String, String> = emptyMap(),
        noinline mapper: ((T) -> R)? = null
    ): ResultWrapper<R> {
        return try {
            val response = client.request(url) {
                this.method = method
                // Apply query parameters
                url {
                    this.parameters.appendAll(Parameters.build {
                        parameters.forEach { (key, value) ->
                            append(key, value)
                        }
                    })
                }
                // Apply headers
                headers.forEach { (key, value) ->
                    header(key, value)
                }
                // Set body for POST, PUT, etc.
                if (body != null) {
                    contentType(ContentType.Application.Json)
                    when (body) {
                        is String -> setBody(body)
                        else -> setBody(Json.encodeToString(body))
                    }
                }
            }.body<T>()
            val result: R = mapper?.invoke(response) ?: response as R
            ResultWrapper.Success(result)
        } catch (e: ClientRequestException) {
            ResultWrapper.Error(e)
        } catch (e: ServerResponseException) {
            ResultWrapper.Error(e)
        } catch (e: IOException) {
            ResultWrapper.Error(e)
        } catch (e: Exception) {
            ResultWrapper.Error(e)
        }
    }



}