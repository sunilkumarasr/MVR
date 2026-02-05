package com.royalit.mvr.Retrofit

import com.royalit.mvr.Model.AboutusResponse
import com.royalit.mvr.Model.ContactUsResponse
import com.royalit.mvr.Model.CreatePasswordRequest
import com.royalit.mvr.Model.CreatePasswordResponse
import com.royalit.mvr.Model.EmailRequest
import com.royalit.mvr.Model.FaqModel
import com.royalit.mvr.Model.ForgotEmailResponse
import com.royalit.mvr.Model.HelpSupportResponse
import com.royalit.mvr.Model.LoginRequest
import com.royalit.mvr.Model.LoginResponse
import com.royalit.mvr.Model.MyTeamsModel
import com.royalit.mvr.Model.NotificationModel
import com.royalit.mvr.Model.OTPRequest
import com.royalit.mvr.Model.OTPResponse
import com.royalit.mvr.Model.PlotsStatusResponse
import com.royalit.mvr.Model.PrivacyPolicyResponse
import com.royalit.mvr.Model.ProfileModel
import com.royalit.mvr.Model.ProjectModel
import com.royalit.mvr.Model.RegisterRequest
import com.royalit.mvr.Model.RegisterResponse
import com.royalit.mvr.Model.SaleModel
import com.royalit.mvr.Model.TermsAndConditionsResponse
import com.royalit.mvr.Model.TransactionsModel
import com.royalit.mvr.Model.UpdateProfileResponse
import com.royalit.mvr.Model.WalletResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiInterface {

    //logins
    @POST("register")
    fun registerApi(@Body registerRequest: RegisterRequest): Call<RegisterResponse>

    @POST("login")
    fun loginApi(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @POST("forgot_password")
    fun forgotEmailApi(@Body loginRequest: EmailRequest): Call<ForgotEmailResponse>

    @POST("verify_otp")
    fun otpApi(@Body loginRequest: OTPRequest): Call<OTPResponse>

    @POST("reset_password")
    fun createApi(@Body createRequest: CreatePasswordRequest): Call<CreatePasswordResponse>

    @GET("get_about_us")
    fun aboutusApi(): Call<AboutusResponse>

    @GET("getTermsConditions")
    fun termsAndConditionsApi(): Call<TermsAndConditionsResponse>

    @GET("privacypolicy")
    fun privacyPolicyApi(): Call<PrivacyPolicyResponse>

    @GET("get_contact_us")
    fun contactUsApi(): Call<ContactUsResponse>

    @FormUrlEncoded
    @POST("HelpSupport")
    fun helpAndSupportApi(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("phone") phone: String,
        @Field("message") message: String
    ): Call<HelpSupportResponse>

    @GET("get_all_transactions")
    fun allTransactionsListApi(@Query("emp_id") id: String?): Call<TransactionsModel>
    @GET("get_completed_transactions")
    fun completedTransactionsListApi(@Query("emp_id") id: String?): Call<TransactionsModel>
    @GET("get_reject_transactions")
    fun rejectedTransactionsListApi(@Query("emp_id") id: String?): Call<TransactionsModel>


    @GET("profile_get")
    fun getProfileApi(@Query("id") id: String?): Call<ProfileModel>

    @Multipart
    @POST("update_profile")
    fun updateProfileApi(
        @Part("user_id") user_id: RequestBody,
        @Part("name") name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part("address") address: RequestBody,
        @Part("city") city: RequestBody,
        @Part image: MultipartBody.Part
    ): Call<UpdateProfileResponse>


    @GET("get_faq")
    fun faqListApi(): Call<FaqModel>

    @GET("get_notifications")
    fun NotificationsListApi(): Call<NotificationModel>

    @GET("get_emp_myteam")
    fun myTeamsListApi(@Query("emp_id") id: String?): Call<MyTeamsModel>

    @GET("get_sales_by_employee")
    fun saleListApi(@Query("emp_id") id: String?): Call<SaleModel>

    @FormUrlEncoded
    @POST("project_enquiry")
    fun enqueryApi(
        @Field("project_id") project_id: String,
        @Field("name") name: String,
        @Field("phone") phone: String,
        @Field("email") email: String,
        @Field("subject") subject: String,
        @Field("message") message: String,
        @Field("created_by") created_by: String
    ): Call<HelpSupportResponse>

    @GET("get_projects")
    fun projectListApi(): Call<ProjectModel>

    @FormUrlEncoded
    @POST("withdraw_request")
    fun AddWalletApi(
        @Field("employee_id") employee_id: String,
        @Field("amount") amount: String,
        @Field("notes") notes: String
    ): Call<WalletResponse>

    @GET("check_balance_get")
    fun getWalletApi(@Query("emp_id") id: String?): Call<WalletResponse>

    @GET("get_plots_status")
    fun getPlotsStatusApi(@Query("project_id") id: String?): Call<PlotsStatusResponse>

}