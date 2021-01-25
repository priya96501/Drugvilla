package com.drugvilla.in.service;


import com.drugvilla.in.model.address.AddressBaseResponse;
import com.drugvilla.in.model.address.CheckPincodeBaseResponse;
import com.drugvilla.in.model.appointment.booking.AppointmentBookingResponse;
import com.drugvilla.in.model.appointment.booking.AppointmentOrderDeatilResponse;
import com.drugvilla.in.model.appointment.booking.ConfirmBookingResponse;
import com.drugvilla.in.model.appointment.booking.review.AppointmentReviewResponse;
import com.drugvilla.in.model.appointment.myAppointment.AppointmentListResponse;
import com.drugvilla.in.model.appointment.myAppointment.AppointmentResponse;
import com.drugvilla.in.model.appointment.timeSlot.TimeSlotResponse;
import com.drugvilla.in.model.authentication.LoginData;
import com.drugvilla.in.model.authentication.BaseResponse;
import com.drugvilla.in.model.blog.BlogResponse;
import com.drugvilla.in.model.blog.blogDetail.BlogDetailResponse;
import com.drugvilla.in.model.blog.comments.CommentResponse;
import com.drugvilla.in.model.dashboard.BannerResponse;
import com.drugvilla.in.model.dashboard.CategoryResponse;
import com.drugvilla.in.model.dashboard.HomeResponse;
import com.drugvilla.in.model.dashboard.labDashboard.LabDashboardResponse;
import com.drugvilla.in.model.doctor.DoctorCategoryResponse;
import com.drugvilla.in.model.doctor.DoctorListResponse;
import com.drugvilla.in.model.doctor.DoctorResponse;
import com.drugvilla.in.model.dashboard.labDashboard.LabBaseResponse;
import com.drugvilla.in.model.cart.CartResponse;
import com.drugvilla.in.model.lab.SelectLabResponse;
import com.drugvilla.in.model.lab.packages.PackageListResponse;
import com.drugvilla.in.model.lab.packages.PackageResponse;
import com.drugvilla.in.model.lab.test.TestListingResponse;
import com.drugvilla.in.model.lab.test.TestResponse;
import com.drugvilla.in.model.lab.labs.LabListingResponse;
import com.drugvilla.in.model.lab.labs.LabResponse;
import com.drugvilla.in.model.lab.myTest.MyTestDetailResponse;
import com.drugvilla.in.model.lab.myTest.MyTestListResponse;
import com.drugvilla.in.model.offer.OfferBaseResponse;
import com.drugvilla.in.model.offer.OfferListResponse;
import com.drugvilla.in.model.order.PrescriptionResponse;
import com.drugvilla.in.model.order.labOrderReview.LabOrderReviewResponse;
import com.drugvilla.in.model.order.myOrder.MyOrderDetailResponse;
import com.drugvilla.in.model.order.myOrder.MyOrderListResponse;
import com.drugvilla.in.model.order.orderDetail.OrderDetailResponse;
import com.drugvilla.in.model.order.orderReview.OrderReviewResponse;
import com.drugvilla.in.model.order.orderReview.fromRX.ReviewResponse;
import com.drugvilla.in.model.order.saveOrder.SaveOrderResponse;
import com.drugvilla.in.model.order.saveOrder.SaveOrderResponse2;
import com.drugvilla.in.model.orderCancellation.CancelllationResponse;
import com.drugvilla.in.model.patient.PatientBaseResponse;
import com.drugvilla.in.model.product.ProductCategoryDetailPageResponse;
import com.drugvilla.in.model.product.ProductListingResponse;
import com.drugvilla.in.model.product.category.ProductCategoryResponse;
import com.drugvilla.in.model.product.filters.FilterResponse;
import com.drugvilla.in.model.product.medicinedetail.MedicineResponse;
import com.drugvilla.in.model.product.productdetail.ProductResponse;
import com.drugvilla.in.model.product.sort.SortingResponse;
import com.drugvilla.in.model.reminder.ReminderBaseResponse;
import com.drugvilla.in.model.searching.SearchingResponse;
import com.drugvilla.in.model.wishlist.WishlistResponse;

import java.util.HashMap;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface Api {

    /*  AUTHENTICATION SECTION  */

    // 1. signup
    @FormUrlEncoded
    @POST("sign.php")
    Call<BaseResponse> getSignup(@Field("f_name") String first_name,
                                 @Field("l_name") String last_name,
                                 @Field("email") String email,
                                 @Field("mobile") String mobile,
                                 @Field("password") String password,
                                 @Field("refered_from") String code);

    // 2. verify otp for signup process
    @FormUrlEncoded
    @POST("checkotp.php")
    Call<LoginData> verifyOtpSignup(@Field("mobile") String mobile,
                                    @Field("email") String email,
                                    @Field("otp") String otp,
                                    @Field("device_token") String token);


    // 3. login through email/mobile and password
    @FormUrlEncoded
    @POST("login.php")
    Call<LoginData> getLoginThroughPassword(@Field("inputdata") String input,
                                            @Field("password") String password,
                                            @Field("device_token") String device_token);

    // 4. login through otp (get otp on email or mobile)
    @FormUrlEncoded
    @POST("sendotplogin.php")
    Call<BaseResponse> getOtp(@Field("inputdata") String input);


    // 5. login through otp (verify otp on email or mobile)
    @FormUrlEncoded
    @POST("loginwithotp.php")
    Call<LoginData> verifyOtpLogin(@Field("inputdata") String input,
                                   @Field("otp") String otp,
                                   @Field("device_token") String device_token);

    // 6. resend otp for signup process
    @FormUrlEncoded
    @POST("resendotp.php")
    Call<BaseResponse> getResendOtp(@Field("mobile") String mobile,
                                    @Field("email") String email);


    // 7. verify otp for forget password and updating email and mobile
    @FormUrlEncoded
    @POST("checkotpforgotpp.php")
    Call<com.drugvilla.in.service.BaseResponse> verifyOtp(@Field("inputdata") String input,
                                                          @Field("otp") String otp);

    // 8. reset password
    @FormUrlEncoded
    @POST("resetpassword.php")
    Call<com.drugvilla.in.service.BaseResponse> resetPassword(@Field("inputdata") String input,
                                                              @Field("password") String password);


    // 9. change password
    @FormUrlEncoded
    @POST("changepassword.php")
    Call<com.drugvilla.in.service.BaseResponse> changePassword(@Field("old_password") String oldPass,
                                                               @Field("new_password") String newPass,
                                                               @Field("user_id") String userId);

    // 10. user logout
    @FormUrlEncoded
    @POST("logout.php")
    Call<com.drugvilla.in.service.BaseResponse> logout(@Field("userid") String userId);

    // 11. send otp to update email or mobile
    @FormUrlEncoded
    @POST("send_otp_update.php")
    Call<BaseResponse> getOtpUpdate(@Field("user_id") String userId,
                                    @Field("inputdata") String input);

    // 12. verify otp to update email or mobile
    @FormUrlEncoded
    @POST("verify_otp_update.php")
    Call<LoginData> verifyOtpUpdate(@Field("user_id") String userId,
                                    @Field("inputdata") String input,
                                    @Field("otp") String otp);

    // 13. post query for contact us
    @FormUrlEncoded
    @POST("contact.php")
    Call<com.drugvilla.in.service.BaseResponse> postQuery(@Field("mobile") String mobile,
                                                          @Field("email") String email,
                                                          @Field("name") String name,
                                                          @Field("message") String message);


    /*   ADDRESS SECTION  (get/add/delete/update) 5 api */

    @FormUrlEncoded // 14.
    @POST("checkpincode.php")
    Call<CheckPincodeBaseResponse> checkPincode(@Field("pincode") String pincode);

    @FormUrlEncoded // 15.
    @POST("addaddress.php")
    Call<com.drugvilla.in.service.BaseResponse> addAddress(
            @Field("user_id") String user_id,
            @Field("name") String name,
            @Field("mobile") String mobile,
            @Field("pincode") String pincode,
            @Field("state") String state,
            @Field("city") String city,
            @Field("address_line1") String adress_line1,
            @Field("address_line2") String address_line2,
            @Field("address_type") String address_type,
            @Field("other_address_name") String other_address_name,
            @Field("is_default") boolean value);

    @FormUrlEncoded // 16.
    @POST("updateaddress.php")
    Call<com.drugvilla.in.service.BaseResponse> updateAddress(
            @Field("user_id") String user_id,
            @Field("address_id") String address_id,
            @Field("name") String name,
            @Field("mobile") String mobile,
            @Field("pincode") String pincode,
            @Field("state") String state,
            @Field("city") String city,
            @Field("address_line1") String adress_line1,
            @Field("address_line2") String address_line2,
            @Field("address_type") String address_type,
            @Field("other_address_name") String other_address_name,
            @Field("is_default") boolean value);

    @FormUrlEncoded // 17.
    @POST("deleteaddress.php")
    Call<com.drugvilla.in.service.BaseResponse> deleteAddress(@Field("user_id") String user_id,
                                                              @Field("address_id") String address_id);

    @FormUrlEncoded // 18.
    @POST("getalladdress.php")
    Call<AddressBaseResponse> getAddressList(@Field("user_id") String user_id);




    /*  PATIENT SECTION (get/add/delete/update)   4 api */

    @FormUrlEncoded  // 19.
    @POST("addpatient.php")
    Call<com.drugvilla.in.service.BaseResponse> addPatient(@Field("user_id") String user_id,
                                                           @Field("name") String name,
                                                           @Field("mobile") String mobile,
                                                           @Field("email") String email,
                                                           @Field("age") String age,
                                                           @Field("gender") String gender);

    @FormUrlEncoded  // 20.
    @POST("deletepatient.php")
    Call<com.drugvilla.in.service.BaseResponse> deletePatient(@Field("user_id") String user_id,
                                                              @Field("patient_id") String patient_id);

    @FormUrlEncoded  // 21.
    @POST("getallpatient.php")
    Call<PatientBaseResponse> getPatientList(@Field("user_id") String user_id);

    @FormUrlEncoded  // 22.
    @POST("updatepatient.php")
    Call<com.drugvilla.in.service.BaseResponse> updatePatient(@Field("user_id") String user_id,
                                                              @Field("patient_id") String patient_id,
                                                              @Field("name") String name,
                                                              @Field("mobile") String mobile,
                                                              @Field("email") String email,
                                                              @Field("age") String age,
                                                              @Field("gender") String gender);


    /*   REMINDER SECTION (get/add/delete/update) 5 api */

    @FormUrlEncoded  // 23.
    @POST("addreminder.php")
    Call<com.drugvilla.in.service.BaseResponse> addReminder(@FieldMap HashMap<String, Object> map);

    @FormUrlEncoded  // 24.
    @POST("deletereminder.php")
    Call<com.drugvilla.in.service.BaseResponse> deleteReminder(@Field("user_id") String user_id,
                                                               @Field("reminder_id") String reminder_id);

    @FormUrlEncoded  // 25.
    @POST("getallreminder.php")
// add pagination in correct api in reminder module
    Call<ReminderBaseResponse> getReminderList(@Field("user_id") String user_id);

    @FormUrlEncoded  // 26.
    @POST("updatereminder.php")
    Call<com.drugvilla.in.service.BaseResponse> updateReminder(@FieldMap HashMap<String, Object> map);


    /*  OFFER SECTION  (get list / get detail)  2 api */

    @FormUrlEncoded  // 27.
    @POST("getofferlisting.php")
    Call<OfferListResponse> getOffersList(@Field("user_id") String user_id);

    @FormUrlEncoded  // 28.
    @POST("getofferdetail.php")
    Call<OfferBaseResponse> getOfferDetail(@Field("user_id") String user_id,
                                           @Field("offer_id") String offer_id);




    /* DOCTOR APPOINTMENT SECTION  (14 api)  */

    // 33. search doctors by name and by category
    @FormUrlEncoded
    @POST("doctorsearch.php")
    Call<SearchingResponse> getDoctorSearch(@Field("search") String input);

    @FormUrlEncoded  // 34.
    @POST("getdoctorcategory.php")
    Call<DoctorCategoryResponse> getDoctorCategory(@Field("page") int page_no);

    @FormUrlEncoded  // 35.
    @POST("getdoctorslist.php")
    Call<DoctorListResponse> getDoctorList(@Field("doctor_category_id") String doctor_category_id,
                                           @Field("page") int page_no);

    @FormUrlEncoded  // 36.
    @POST("getdoctorprofile.php")
    Call<DoctorResponse> getDoctorProfile(@Field("doctor_id") String doctor_id);

    @FormUrlEncoded  // 37.
    @POST("adddoctorreview.php")
    Call<com.drugvilla.in.service.BaseResponse> addDoctorReview(@Field("user_id") String user_id,
                                                                @Field("doctor_id") String doctor_id,
                                                                @Field("user_rating") int user_rating,
                                                                @Field("user_review") String user_review);

    @FormUrlEncoded  // 38.
    @POST("gettimeslot.php")
    Call<TimeSlotResponse> getTimeSlot(@Field("doctor_id") String doctor_id,
                                       @Field("day") String day,
                                       @Field("date") String date);


    @FormUrlEncoded  // 39.
    @POST("addappointment.php")
    Call<AppointmentBookingResponse> addAppointment(@Field("user_id") String user_id,
                                                    @Field("doctor_id") String doctor_id,
                                                    @Field("date") String date,
                                                    @Field("time") String time,
                                                    @Field("day") String day,
                                                    @Field("selected_patient_id") String selected_patient_id);

    @FormUrlEncoded  // 40.
    @POST("getappointmentorderreview.php")
    Call<AppointmentReviewResponse> getAppointmentReview(@Field("user_id") String user_id,
                                                         @Field("appointment_order_id") String appointment_id);

    @FormUrlEncoded  // 41.
    @POST("confirmappointment.php")
    Call<ConfirmBookingResponse> confirmAppointment(@Field("user_id") String user_id,
                                                    @Field("appointment_order_id") String appointment_id);

    @FormUrlEncoded  // 42.
    @POST("getappointmentorderdetail.php")
    Call<AppointmentOrderDeatilResponse> getappointmentorderdetail(@Field("user_id") String user_id,
                                                                   @Field("appointment_order_id") String appointment_order_id);

    @FormUrlEncoded  // 43.
    @POST("getappointmentdetail.php")
    Call<AppointmentResponse> getAppointmentDetail(@Field("user_id") String user_id,
                                                   @Field("appointment_id") String appointment_id);


    @FormUrlEncoded  // 44.
    @POST("deleteappointment.php")
    Call<com.drugvilla.in.service.BaseResponse> deleteAppointment(@Field("user_id") String user_id,
                                                                  @Field("appointment_id") String appointment_id);

    @FormUrlEncoded  // 45.
    @POST("updateappointment.php")
    Call<com.drugvilla.in.service.BaseResponse> updateAppointment(@Field("user_id") String user_id,
                                                                  @Field("appointment_id") String appointment_id,
                                                                  @Field("appointment_date") String appointment_date,
                                                                  @Field("appointment_time") String appointment_time);

    @FormUrlEncoded  // 46.
    @POST("getallappointments.php")
    Call<AppointmentListResponse> getAllApointment(@Field("user_id") String user_id, @Field("page") int page_no);






    /*   LAB/TEST/PACKAGE SECTION  23 api */


    // 47.search for test/package/labs
    @FormUrlEncoded
    @POST("labsearch.php")
// TODO: check
    Call<SearchingResponse> getLabSearch(@Field("search") String input);

    // lab dashboard data


    @GET("tests.php")
// 50.
    Call<LabDashboardResponse> getTests();

    @GET("packages.php")
// 51.
    Call<LabDashboardResponse> getPackages();

    @GET("labs.php")
// 52.
    Call<LabBaseResponse> getLabs();


    //  test data section
    @FormUrlEncoded  // 53.
    @POST("getalltest.php")
    Call<TestListingResponse> getAllTest(@Field("page") int page_no);

    @FormUrlEncoded  // 54.
    @POST("gettestdetail.php")
    Call<TestResponse> getTestDetail(@Field("test_id") String test_id);

    // labs data section
    @FormUrlEncoded  // 55.
    @POST("getlabs.php")
    Call<LabListingResponse> getAllLabs(@Field("page") int page_no);

    @FormUrlEncoded  // 56.
    @POST("getlabdetail.php")
    Call<LabResponse> getLabDetail(@Field("lab_id") String lab_id);

    // package data section
    @FormUrlEncoded  // 57.
    @POST("getallpackages.php")
    Call<PackageListResponse> getAllPackage(@Field("page") int page_no);

    @FormUrlEncoded  // 58.
    @POST("getpackagedetail.php")
    Call<PackageResponse> getPackageDetail(@Field("package_id") String package_id);


    // lab cart and order section

    @FormUrlEncoded  // 62.
    @POST("selectlablisting.php")
    Call<SelectLabResponse> getSelectLabListing(@Field("test_id") String test_id);


    @FormUrlEncoded  // 63.
    @POST("addtotestcart.php")
    Call<com.drugvilla.in.service.BaseResponse> addToLabTestCart(HashMap<String, String> map);

    @FormUrlEncoded  // 64.
    @POST("gettestcart.php")
// TODO: check
    Call<CartResponse> getLabTestCart(@Field("user_id") String user_id);

    @FormUrlEncoded  // 65.
    @POST("removeitem.php")
    Call<com.drugvilla.in.service.BaseResponse> removeLabCartItem(@Field("user_id") String user_id,
                                                                  @Field("item_id") String item_id);

    @FormUrlEncoded  // 66.
    @POST("getlabtime.php")
// TODO: check
    Call<TimeSlotResponse> getLabTimeSlot(@Field("user_id") String user_id,
                                          @Field("selected_lab_id") String selected_lab_id,
                                          @Field("date") String date,
                                          @Field("day") String day);

    @FormUrlEncoded  // 67.
    @POST("savetestorderdetail.php")
// TODO: check
    Call<SaveOrderResponse> saveLabTestOrderDetails(HashMap<String, String> map);


    @FormUrlEncoded  // 68.
    @POST("getlabtestorderreview.php")
// TODO: check
    Call<LabOrderReviewResponse> getLabTestOrderReview(@Field("user_id") String user_id,
                                                       @Field("order_id") String order_id);

    @FormUrlEncoded  // 69.
    @POST("confirmtestorder.php")
// TODO: check
    Call<SaveOrderResponse> confirmLabTestOrder(@Field("user_id") String user_id,
                                                @Field("payment_type") String payment_type,
                                                @Field("order_id") String order_id);

    @FormUrlEncoded  // 70.
    @POST("getlabtestdetail.php")
        // TODO: check
    Call<OrderDetailResponse> getLabOrderDetail(@Field("user_id") String user_id,
                                                @Field("order_id") String order_id);

    // my lab test section (get list/get detail/cancel lab order)
    @FormUrlEncoded  // 59.
    @POST("getalllabtestorder.php")
    Call<MyOrderListResponse> getMyTestList(@Field("user_id") String user_id,
                                            @Field("page") int page_no);

    @FormUrlEncoded  // 60.
    @POST("getlabtestorderdetail.php")
        // TODO: check & Changes as per new api response
    Call<MyTestDetailResponse> getMyTestDetail(@Field("user_id") String user_id,
                                               @Field("labtest_id") String labtest_id);

    @FormUrlEncoded  // 61.
    @POST("cancellab.php")
        // TODO: check
    Call<com.drugvilla.in.service.BaseResponse> cancelLab(@Field("user_id") String user_id,
                                                          @Field("lab_id") String order_id,
                                                          @Field("reason_id") String reason_id);




    /*  PRODUCT/MEDICINE SECTION   3 api*/

    // 71.product/medicine searching
    @FormUrlEncoded
    @POST("medicinesearch.php")
    Call<SearchingResponse> getProductSearch(@Field("search") String input);

    // 72.medicine detail
    @FormUrlEncoded
    @POST("getmedicinedetail.php")
    Call<MedicineResponse> getMedicineDetail(@Field("product_id") String product_id);

    // 73.product detail
    @FormUrlEncoded
    @POST("getproductdetail.php")
    Call<ProductResponse> getProductDetail(@Field("product_id") String product_id);

    // Product category listing
    @GET("getproductcategory")
    Call<ProductCategoryResponse> getAllProductCategory();

    // Product category detail page
    @FormUrlEncoded
    @POST("getproductcategorydetail.php")
    Call<ProductCategoryDetailPageResponse> getProductCategoryDetailData(@Field("product_category_id") String product_category_id);

    // Product listing( category and sub category wise )
    @FormUrlEncoded
    @POST("productlisting")
    Call<ProductListingResponse> getProductListing(HashMap<String, String> map);



    /* PRODUCT SORT/FILTER SECTION */

    @GET("getsortlist.php")
    Call<SortingResponse> getSortingList();

    @GET("getfilters.php")
    Call<FilterResponse> getFiltersListing();


    /* PRODUCT CART SECTION */
    @FormUrlEncoded
    @POST("addtocart.php")
    Call<com.drugvilla.in.service.BaseResponse> addToCart(@Field("user_id") String user_id,
                                                          @Field("item_id") String item_id,
                                                          @Field("item_quantity") String item_quantity);

    @FormUrlEncoded
    @POST("removecartitem.php")
    Call<com.drugvilla.in.service.BaseResponse> removeCartItem(@Field("user_id") String user_id,
                                                               @Field("item_id") String item_id);

    @FormUrlEncoded
    @POST("getcart.php")
    Call<CartResponse> getCart(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("updatecart.php")
    Call<com.drugvilla.in.service.BaseResponse> updateCart(@Field("user_id") String user_id,
                                                           @Field("item_id") String item_id,
                                                           @Field("item_quantity") String item_quantity);

    /* PRESCRIPTION SECTION */ // TODO : check

    @FormUrlEncoded
    @POST("myprescription.php")
    Call<PrescriptionResponse> getMyPrescriptions(@Field("user_id") String user_id);

    @Multipart
    @POST("uploadprescription.php")
    Call<PrescriptionResponse> uploadPrescriptions(@Part("user_id") RequestBody user_id,
                                                   @Part MultipartBody.Part file);


    /* PRODUCT ORDER PROCESS SECTION from normal order flow */ // TODO : check

    @FormUrlEncoded
    @POST("saveorder.php")
    Call<SaveOrderResponse> saveOrderDetail(@FieldMap HashMap<String, String> map);

    @FormUrlEncoded
    @POST("getorderreview.php")
    Call<OrderReviewResponse> getOrderReview(@Field("user_id") String user_id,
                                             @Field("order_id") String order_id);

    @FormUrlEncoded
    @POST("confirmorder.php")
    Call<SaveOrderResponse2> confirmOrder(@FieldMap HashMap<String, String> map);

    @FormUrlEncoded
    @POST("getorderdetail.php")
    Call<OrderDetailResponse> getOrderDetail(@Field("user_id") String user_id,
                                             @Field("order_id") String order_id);


    /* PRODUCT ORDER PROCESS SECTION from Upload RX flow */ // TODO : check
    @FormUrlEncoded
    @POST("saveprescriptionorder.php")
    Call<SaveOrderResponse> savePrescriptionOrderDetail(@FieldMap HashMap<String, String> map);

    @FormUrlEncoded
    @POST("prescriptionorderreview.php")
    Call<ReviewResponse> getPrescriptionOrderReview(@Field("user_id") String user_id,
                                                    @Field("order_id") String order_id);

    @FormUrlEncoded
    @POST("confirmprescriptionorderreview.php")
    Call<SaveOrderResponse> confirmPrescriptionOrder(@Field("user_id") String user_id,
                                                     @Field("order_id") String order_id);




    /*  MY ORDER SECTION (get list/get detail)  */// TODO: check

    @FormUrlEncoded
    @POST("getallorder.php")
    Call<MyOrderListResponse> getMyOrderList(@Field("user_id") String user_id, @Field("page") int page_no);

    @FormUrlEncoded
    @POST("getorderdetail.php")
    Call<MyOrderDetailResponse> getMyOrderDetail(@Field("user_id") String user_id,
                                                 @Field("order_id") String order_id);

    @FormUrlEncoded
    @POST("getcancelorderreasonlisting.php")
    Call<CancelllationResponse> getCancellationReason(@Field("reason_type") String reason_type);


    // 32. order cancellation reasons list // TODO: check
    @FormUrlEncoded
    @POST("cancelorder.php")
    Call<com.drugvilla.in.service.BaseResponse> cancelOrder(@Field("user_id") String user_id,
                                                            @Field("order_id") String order_id,
                                                            @Field("reason_id") String reason_id);


    // WISHLIST SECTION (add,get,remove,move to cart)//
    @FormUrlEncoded
    @POST("addwishlist.php")
    Call<com.drugvilla.in.service.BaseResponse> addToWishlist(@Field("user_id") String userId,
                                                              @Field("item_id") String itemId);

    @FormUrlEncoded
    @POST("wishlist.php")
    Call<WishlistResponse> getWishlist(@Field("user_id") String userId);


    @FormUrlEncoded
    @POST("removewishlist.php")
    Call<com.drugvilla.in.service.BaseResponse> removeFromWishlist(@Field("user_id") String userId,
                                                                   @Field("id") String id);

    @FormUrlEncoded
    @POST("movecartwishlist.php")
    Call<com.drugvilla.in.service.BaseResponse> moveToCart(@Field("user_id") String userId,
                                                           @Field("id") String id);




    /*  BLOG SECTION  10 api */

    @GET("getblogcategory.php")
    Call<CategoryResponse> getBlogCategory();

    @GET("getallblogs.php")
    Call<BlogResponse> getAllBlogs();

    @FormUrlEncoded
    @POST("getfavouriteblogs.php")
    Call<BlogResponse> getFavouriteBlogs(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("getblogdetail.php")
    Call<BlogDetailResponse> getBlogDetail(@Field("blog_id") String blog_id);

    @FormUrlEncoded
    @POST("getcategoryblogs.php")
    Call<BlogResponse> getCategoryWiseBlog(@Field("blog_category_id") String blog_category_id);

    @FormUrlEncoded
    @POST("getblogcomments.php")
    Call<CommentResponse> getBlogComments(@Field("blog_id") String blog_id,
                                          @Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("deleteblogcomment.php")
    Call<com.drugvilla.in.service.BaseResponse> deleteComment(@Field("user_id") String user_id,
                                                              @Field("blog_id") String blog_id,
                                                              @Field("id") String id);

    @FormUrlEncoded
    @POST("editblogcomment.php")
    Call<com.drugvilla.in.service.BaseResponse> updateComment(@Field("user_id") String user_id,
                                                              @Field("blog_id") String blog_id,
                                                              @Field("id") String id,
                                                              @Field("name") String name,
                                                              @Field("email") String email,
                                                              @Field("comment") String comment);

    @FormUrlEncoded
    @POST("replyblogcomment.php")
    Call<com.drugvilla.in.service.BaseResponse> replyOnComment(
            @Field("user_id") String user_id,
            @Field("blog_id") String blog_id,
            @Field("name") String name,
            @Field("email") String email,
            @Field("comment") String comment,
            @Field("comment_id") String comment_id,
            @Field("reply_to") String reply_to);

    @FormUrlEncoded
    @POST("addblogcomment.php")
    Call<com.drugvilla.in.service.BaseResponse> addComment(
            @Field("user_id") String user_id,
            @Field("blog_id") String blog_id,
            @Field("name") String name,
            @Field("email") String email,
            @Field("comment") String comment);

    @FormUrlEncoded
    @POST("addfavblog.php")
    Call<com.drugvilla.in.service.BaseResponse> addFavouriteBlog(
            @Field("user_id") String user_id,
            @Field("blog_id") String blog_id);


    // DASHBOARD API

    @FormUrlEncoded
    @POST("getbanners.php")
    Call<BannerResponse> getBanners(@Field("banner_type ") String bannerType);

    @FormUrlEncoded
    @POST("getcategory.php")
    Call<CategoryResponse> getCategory(@Field("type") String type);


    @GET("index.php")
    Call<HomeResponse> getHomePageData();


}
