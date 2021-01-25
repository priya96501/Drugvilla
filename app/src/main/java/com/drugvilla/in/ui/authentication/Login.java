package com.drugvilla.in.ui.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.drugvilla.in.R;
import com.drugvilla.in.databinding.ActivityLoginBinding;
import com.drugvilla.in.model.authentication.BaseResponse;
import com.drugvilla.in.model.authentication.LoginData;
import com.drugvilla.in.service.AESCrypt;
import com.drugvilla.in.service.Api;
import com.drugvilla.in.service.RequestController;
import com.drugvilla.in.ui.startAndDashboard.Home;
import com.drugvilla.in.utils.ActivityController;
import com.drugvilla.in.utils.AppConstant;
import com.drugvilla.in.utils.CommonUtils;
import com.drugvilla.in.utils.PrefManager;
import com.drugvilla.in.utils.ProgressDialogUtils;
import com.drugvilla.in.utils.RegexUtils;
import com.drugvilla.in.utils.SharedPref;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    private ActivityLoginBinding binding;
    private Context context;
    private String otp1;
    private String otp3;
    private String otp4;
    private String otp2;
    private String name;
    private String email;
    private String idToken;
    private String input, password;
    private int flag = 1;
    private Activity activity;
    private GoogleApiClient googleApiClient;
    private static final int RC_SIGN_IN = 1;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private static final String TAG = "Login";
    CallbackManager callbackManager;
    private String secretKey, QrCode, from = " ", device_token = "";
    HashMap<String, String> map = new HashMap<>();
    private boolean throughOTP = false;
    private String encrypt_password, decrypt_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        context = Login.this;
        activity = Login.this;
        // fetching device token from prefrence manager
        PrefManager prefManager = PrefManager.getInstance(activity);
        device_token = prefManager.getPreference(AppConstant.DEVICE_TOKEN_);
        // setting status bar color
        CommonUtils.changeStatusBarColor(activity);


        getIntentData();
        setToolbar();
        setViews();
        addGoogle();
        addFacebook();
    }

    // when api will be applied for the qr code and secret key value will be placed
    private void getIntentData() {
        Bundle intent = activity.getIntent().getExtras();
        if (intent != null) {
            if (intent.getString(AppConstant.FROM) != null) {
                from = intent.getString(AppConstant.FROM);
                /*if (intent.getString(AppConstant.OR_CODE) != null && !Objects.requireNonNull(intent.getString(AppConstant.OR_CODE)).isEmpty()) {
                    QrCode = intent.getString(AppConstant.OR_CODE);
                    Picasso.with(context).load(QrCode).placeholder(R.drawable.scancode).into(binding.ivKey);
                }
                if (intent.getString(AppConstant.SECRET_KEY) != null && !Objects.requireNonNull(intent.getString(AppConstant.SECRET_KEY)).isEmpty()) {
                    secretKey = intent.getString(AppConstant.SECRET_KEY);
                    binding.tvKey.setText(secretKey);
                }*/
            }
        }
    }

    private void addFacebook() {
        callbackManager = CallbackManager.Factory.create();
        binding.facebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.d("TAG", object.toString());
                                try {
                                    Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show();
                                    gotoHome(AppConstant.TYPE_SOCIAL_LOGIN_FACEBOOK);
                                    // String first_name = object.getString("first_name");
                                    //String last_name = object.getString("last_name");
                                    // String email = object.getString("email");
                                    String id = object.getString("id");
                                    //String image_url = "https://graph.facebook.com/" + id + "/picture?type=normal";
                                } catch (Exception e) {
                                    System.out.println("jscgh" + e);
                                    e.printStackTrace();
                                }
                            }
                        });
                request.executeAsync();
                //boolean loggedIn = AccessToken.getCurrentAccessToken() == null;
                // Log.d("API123", loggedIn + " ??");
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });
    }

       private void addGoogle() {
        firebaseAuth = com.google.firebase.auth.FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_client_id))
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }


    private void setToolbar() {
        // binding.menubar.ivBack.setImageResource(R.drawable.ic_keyboard_backspace_black_24dp);
        // binding.menubar.ivBack.setOnClickListener(this);
        //  binding.menubar.tvTitle.setVisibility(View.VISIBLE);
        //   binding.menubar.tvTitle.setText("Login");
    }

    private void setViews() {
        setselectedBackground(binding.loginThroughPassword, binding.loginThroughOtp);
        SpannableString spannableStr = new SpannableString("New user? Sign Up");
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.RED);
        spannableStr.setSpan(foregroundColorSpan, 10, 17, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        binding.tvNewUser.setText(spannableStr);
        binding.tvNewUser.setOnClickListener(this);
        binding.ivBack.setOnClickListener(this);
        binding.loginThroughOtp.setOnClickListener(this);
        //   binding.loginThroughScan.setOnClickListener(this);
        binding.loginThroughPassword.setOnClickListener(this);
        binding.tvResendOtp.setOnClickListener(this);
        binding.tvForgotPassword.setOnClickListener(this);
        binding.btnLogin.setOnClickListener(this);
        // binding.ivCopy.setOnClickListener(this);
        binding.gmail.setOnClickListener(this);
        binding.tvResendOtp.setOnClickListener(this);
        binding.etOtp1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 1) {
                    binding.etOtp2.requestFocus();
                }
                binding.etOtp1.setBackground(getResources().getDrawable(R.drawable.back_white));
                binding.tvOtpError.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.etOtp2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 1) {
                    binding.etOtp3.requestFocus();
                } else {
                    binding.etOtp1.requestFocus();
                }
                binding.etOtp2.setBackground(getResources().getDrawable(R.drawable.back_white));
                binding.tvOtpError.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.etOtp3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 1) {
                    binding.etOtp4.requestFocus();
                } else {
                    binding.etOtp2.requestFocus();
                }
                binding.etOtp3.setBackground(getResources().getDrawable(R.drawable.back_white));
                binding.tvOtpError.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.etOtp4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.length() == 0) {
                    binding.etOtp3.requestFocus();
                }
                binding.etOtp4.setBackground(getResources().getDrawable(R.drawable.back_white));
                binding.tvOtpError.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.etEmailMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                binding.tvInputError.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                binding.tvPasswordError.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.etKey.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                binding.tvKeyError.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
            if (account != null) {
                idToken = account.getIdToken();
                name = account.getDisplayName();
                email = account.getEmail();
            }
            AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
            firebaseAuthWithGoogle(credential);
        } else {
            Log.e(TAG, "Login Unsuccessful. " + result);
            Toast.makeText(this, "Login Unsuccessful", Toast.LENGTH_SHORT).show();
        }
    }

    private void firebaseAuthWithGoogle(AuthCredential credential) {

        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
                        if (task.isSuccessful()) {
                            Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show();
                            gotoHome(AppConstant.TYPE_SOCIAL_LOGIN_GOOGLE);
                        } else {
                            Log.w(TAG, "signInWithCredential" + Objects.requireNonNull(task.getException()).getMessage());
                            task.getException().printStackTrace();
                            Toast.makeText(context, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    private void gotoHome(String LoginType) {
        Bundle bundle = new Bundle();
        Intent intent = new Intent(context, Home.class);
        SharedPref.saveBooleanPreferences(context, AppConstant.IS_LOGIN, true);
        SharedPref.saveStringPreference(context, AppConstant.LOGIN_TYPE, LoginType);
        bundle.putString(AppConstant.FROM, AppConstant.LOGIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtras(bundle);
        startActivity(intent);
        finishAffinity();
    }


    private void copyText(String Key) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("label", Key);
        if (clipboard != null) {
            clipboard.setPrimaryClip(clip);
            Toast.makeText(context, "Text Copied", Toast.LENGTH_SHORT).show();
          /*  new StyleableToast.Builder(activity)
                    .text("Text Copied")
                    .textColor(ContextCompat.getColor(activity, R.color.colorWhite))
                    .backgroundColor(ContextCompat.getColor(activity, R.color.colorRed))
                    .show();*/
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.gmail:
                intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent, RC_SIGN_IN);
                break;
            case R.id.login_through_otp:
                flag = 2;
                setselectedBackground(binding.loginThroughOtp, binding.loginThroughPassword);
                //  setUnselectedBackground(binding.loginThroughPassword, binding.loginThroughScan);
                binding.emailMobile.setVisibility(View.VISIBLE);
                binding.llPassword.setVisibility(View.GONE);
                //  binding.ll2FA.setVisibility(View.GONE);
                binding.btnLogin.setText(getResources().getString(R.string.send_otp));
                break;
           /* case R.id.login_through_scan:
                flag = 3;
                setselectedBackground(binding.loginThroughScan);
                setUnselectedBackground(binding.loginThroughPassword, binding.loginThroughOtp);
                binding.llPassword.setVisibility(View.GONE);
                binding.llOtp.setVisibility(View.GONE);
                binding.emailMobile.setVisibility(View.GONE);
                binding.ll2FA.setVisibility(View.VISIBLE);
                binding.tvInputError.setVisibility(View.GONE);
                break;*/
            case R.id.login_through_password:
                flag = 1;
                setselectedBackground(binding.loginThroughPassword, binding.loginThroughOtp);
                // setUnselectedBackground(binding.loginThroughOtp, binding.loginThroughScan);
                binding.llOtp.setVisibility(View.GONE);
                //   binding.ll2FA.setVisibility(View.GONE);
                binding.emailMobile.setVisibility(View.VISIBLE);
                binding.llPassword.setVisibility(View.VISIBLE);
                binding.etOtp1.getText().clear();
                binding.etOtp2.getText().clear();
                binding.etOtp3.getText().clear();
                binding.etOtp4.getText().clear();
                binding.btnLogin.setText(getResources().getString(R.string.login));

                break;
            case R.id.ivBack:
                finish();
                break;
            /*case R.id.ivCopy:
                copyText(binding.tvKey.getText().toString().trim());
                break;*/
            case R.id.tvNewUser:
                ActivityController.startActivity(context, Signup.class);
                break;
            case R.id.btnLogin:
                // login_through_otp:
                if (flag == 2) {
                    if (!throughOTP) {
                        if (checkEmailMobile()) {
                            //send otp on email or mobile api
                            getOtpOnEmailMobile();
                        }
                    } else {
                        if (isValidOtp()) {
                            String OTP = otp1 + otp2 + otp3 + otp4;
                            // verify otp send on email or mobile for login
                            verifyOtpForLogin(OTP);
                        }
                    }
                }

              /*  // login_through_scan:
                else if (flag == 3) {
                    if (isValidOrCode()) {
                        gotoHome(AppConstant.TYPE_APPLICATION);
                    }
                }*/

                // login_through_password:
                else {
                    if (checkEmailMobile()) {
                        if (isValidPassword()) {
                            getLoginThroughPasswordApi();
                        }
                    }
                }
                break;
            case R.id.tvForgotPassword:
                ActivityController.startActivity(context, ForgetPassword.class);
                break;
            case R.id.tvResendOtp:
                // Resend otp on email or mobile api
                getOtpOnEmailMobile();
                break;
            default:
                break;
        }
    }


    // user login validations
    private boolean checkEmailMobile() {
        input = Objects.requireNonNull(binding.etEmailMobile.getText()).toString().trim();
        if (input.isEmpty()) {
            CommonUtils.setErrorMessage(binding.etEmailMobile, binding.tvInputError, getResources().getString(R.string.empty_email_mobile));
            return false;
        }
        if (input.contains("@")) {
            if (!RegexUtils.isValidEmail(input)) {
                CommonUtils.setErrorMessage(binding.etEmailMobile, binding.tvInputError, getResources().getString(R.string.invalid_email));
                return false;
            }
        } else {
            if (!RegexUtils.isValidMobileNumber(input)) {
                CommonUtils.setErrorMessage(binding.etEmailMobile, binding.tvInputError, getResources().getString(R.string.invalid_mobile));
                return false;
            }
        }
        return true;
    }

    private boolean isValidOrCode() {
        if (TextUtils.isEmpty(Objects.requireNonNull(binding.etKey.getText()).toString().trim())) {
            CommonUtils.setErrorMessage(binding.etKey, binding.tvKeyError, getResources().getString(R.string.empty_code));
        } else if (TextUtils.getTrimmedLength(binding.etKey.getText().toString().trim()) < 6) {
            CommonUtils.setErrorMessage(binding.etKey, binding.tvKeyError, getResources().getString(R.string.invalid_code));
        } else {
            return true;
        }
        return false;
    }

    private boolean isValidOtp() {
        otp1 = binding.etOtp1.getText().toString().trim();
        otp2 = binding.etOtp2.getText().toString().trim();
        otp3 = binding.etOtp3.getText().toString().trim();
        otp4 = binding.etOtp4.getText().toString().trim();
        if (otp1.isEmpty()) {
            CommonUtils.setErrorMessage(binding.etOtp1, binding.tvOtpError, getResources().getString(R.string.invalid_otp));
            binding.etOtp1.setBackground(getResources().getDrawable(R.drawable.border_red));
        } else if (otp2.isEmpty()) {
            CommonUtils.setErrorMessage(binding.etOtp2, binding.tvOtpError, getResources().getString(R.string.invalid_otp));
            binding.etOtp2.setBackground(getResources().getDrawable(R.drawable.border_red));
        } else if (otp3.isEmpty()) {
            CommonUtils.setErrorMessage(binding.etOtp3, binding.tvOtpError, getResources().getString(R.string.invalid_otp));
            binding.etOtp3.setBackground(getResources().getDrawable(R.drawable.border_red));
        } else if (otp4.isEmpty()) {
            CommonUtils.setErrorMessage(binding.etOtp4, binding.tvOtpError, getResources().getString(R.string.invalid_otp));
            binding.etOtp4.setBackground(getResources().getDrawable(R.drawable.border_red));
        } else if (otp1.isEmpty() && otp2.isEmpty() && otp3.isEmpty() && otp4.isEmpty()) {
            setErrorBackground();
            binding.tvOtpError.setVisibility(View.VISIBLE);
            binding.tvOtpError.setText(getResources().getString(R.string.empty_otp));
        } else {
            return true;
        }
        return false;
    }

    private boolean isValidPassword() {
        password = Objects.requireNonNull(binding.etPassword.getText()).toString().trim();
        if (password.isEmpty()) {
            CommonUtils.setErrorMessage(binding.etPassword, binding.tvPasswordError, getResources().getString(R.string.empty_password));
        } else if (password.length() < 6 || password.length() > 16) {
            CommonUtils.setErrorMessage(binding.etPassword, binding.tvPasswordError, getResources().getString(R.string.invalid_password));
        } else {
            return true;
        }
        return false;
    }

    private void setUnselectedBackground(TextView textView1, TextView textView2) {
        textView1.setBackground(getResources().getDrawable(R.drawable.gray_border));
        textView1.setTextColor(getResources().getColor(R.color.colorBlack));
        textView2.setBackground(getResources().getDrawable(R.drawable.gray_border));
        textView2.setTextColor(getResources().getColor(R.color.colorBlack));
    }

    private void setselectedBackground(TextView selected_textView, TextView unselected_textView) {
        selected_textView.setBackground(getResources().getDrawable(R.drawable.back_red_square));
        selected_textView.setTextColor(getResources().getColor(R.color.colorWhite));
        unselected_textView.setBackground(getResources().getDrawable(R.drawable.gray_border));
        unselected_textView.setTextColor(getResources().getColor(R.color.colorBlack));
    }

    private void setErrorBackground() {
        binding.etOtp1.setBackground(getResources().getDrawable(R.drawable.border_red));
        binding.etOtp2.setBackground(getResources().getDrawable(R.drawable.border_red));
        binding.etOtp3.setBackground(getResources().getDrawable(R.drawable.border_red));
        binding.etOtp4.setBackground(getResources().getDrawable(R.drawable.border_red));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (authStateListener != null) {
            FirebaseAuth.getInstance().signOut();
            firebaseAuth.addAuthStateListener(authStateListener);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (authStateListener != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }


    // login through otp ( verify otp send on email or mobile )
    private void verifyOtpForLogin(String otp) {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                api.verifyOtpLogin(input, otp, device_token).enqueue(new Callback<LoginData>() {
                    @Override
                    public void onResponse(@NonNull Call<LoginData> call, @NonNull Response<LoginData> response) {
                        ProgressDialogUtils.dismiss();
                        if (response.body() != null) {
                            if (response.body().getStatus().equals("1")) {
                                CommonUtils.showToastShort(context, "Login through OTP Successfully.");
                                throughOTP = false;
                                String User_Name = new StringBuilder().append(response.body().getFName()).append(" ").append(response.body().getLName()).toString();
                                System.out.println("user name :  " + User_Name);
                                //  LoginData data = response.body();

                                SharedPref.saveStringPreference(context, AppConstant.USER_NAME, User_Name);
                                SharedPref.saveStringPreference(context, AppConstant.USER_MOBILE, response.body().getMobile());
                                SharedPref.saveStringPreference(context, AppConstant.USER_EMAIL, response.body().getEmail());
                                SharedPref.saveStringPreference(context, AppConstant.USER_ID, response.body().getUserId());
                                SharedPref.saveStringPreference(context, AppConstant.USER_REFERAL_CODE, response.body().getReferalCode());
                                gotoHome(AppConstant.TYPE_APPLICATION);
                            } else {
                                CommonUtils.showToastShort(context, "Invalid OTP.");
                            }
                        } else {
                            CommonUtils.showToastShort(context, getResources().getString(R.string.no_response));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<LoginData> call, @NonNull Throwable t) {
                        ProgressDialogUtils.dismiss();
                        CommonUtils.showToastShort(context, getResources().getString(R.string.failure));
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            CommonUtils.showToastShort(context, getResources().getString(R.string.no_internet));
        }
    }

    private void getInputForEmail_Mobile(String input) {
        if (input.contains("@")) {
            map.put("email", input);
        } else {
            map.put("mobile", input);
        }
    }

    // login through otp ( send otp on email or mobile )
    private void getOtpOnEmailMobile() {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                api.getOtp(input).enqueue(new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<BaseResponse> call, @NonNull Response<BaseResponse> response) {
                        ProgressDialogUtils.dismiss();
                        if (response.body() != null) {
                            if (response.body().getStatus().equals("1")) {
                                CommonUtils.showToastShort(context, "OTP send Successfully.");
                                binding.llOtp.setVisibility(View.VISIBLE);
                                binding.btnLogin.setText(getResources().getString(R.string.login));
                                throughOTP = true;
                            } else {
                                CommonUtils.showToastShort(context, "User not registered..");
                            }
                        } else {
                            CommonUtils.showToastShort(context, getResources().getString(R.string.no_response));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<BaseResponse> call, @NonNull Throwable t) {
                        ProgressDialogUtils.dismiss();
                        CommonUtils.showToastShort(context, getResources().getString(R.string.failure));
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            CommonUtils.showToastShort(context, "" + R.string.no_internet);
        }
    }

    // login through email/mobile and password
    private void getLoginThroughPasswordApi() {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                Api api = RequestController.createService(context, Api.class);
                api.getLoginThroughPassword(input, password, device_token).enqueue(new Callback<LoginData>() {
                    @Override
                    public void onResponse(@NonNull Call<LoginData> call, @NonNull Response<LoginData> response) {
                        ProgressDialogUtils.dismiss();
                        if (response.body() != null) {
                            if (response.body().getStatus().equals("1")) {
                                CommonUtils.showToastShort(context, "Login through password Successfully.");
                                String User_Name = new StringBuilder().append(response.body().getFName()).append(" ").append(response.body().getLName()).toString();
                                System.out.println("user name :  " + User_Name);
                                SharedPref.saveStringPreference(context, AppConstant.USER_NAME, User_Name);
                                SharedPref.saveStringPreference(context, AppConstant.USER_MOBILE, response.body().getMobile());
                                SharedPref.saveStringPreference(context, AppConstant.USER_EMAIL, response.body().getEmail());
                                SharedPref.saveStringPreference(context, AppConstant.USER_ID, response.body().getUserId());
                                SharedPref.saveStringPreference(context, AppConstant.USER_REFERAL_CODE, response.body().getReferalCode());
                                gotoHome(AppConstant.TYPE_APPLICATION);
                            } else {
                                CommonUtils.showToastShort(context, "Invalid password.");
                            }
                        } else {
                            CommonUtils.showToastShort(context, getResources().getString(R.string.no_response));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<LoginData> call, @NonNull Throwable t) {
                        ProgressDialogUtils.dismiss();
                        CommonUtils.showToastShort(context, getResources().getString(R.string.failure));
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            CommonUtils.showToastShort(context, getResources().getString(R.string.no_internet));
        }
    }

    public String setDecrypt_password() {
        try {
            decrypt_password = AESCrypt.decrypt(encrypt_password);
            Toast.makeText(context, "ggd  " + decrypt_password, Toast.LENGTH_SHORT).show();
            System.out.println("decrypt_password  " + decrypt_password);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("decrypt_password  " + e);
        }
        return decrypt_password;
    }

    public String setEncrypt_password() {
        try {
            encrypt_password = AESCrypt.encrypt(binding.etPassword.getText().toString());
            Toast.makeText(context, "ggd  " + encrypt_password, Toast.LENGTH_SHORT).show();
            System.out.println("encrypt_password  " + encrypt_password);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("encrypt_password  " + e);
        }
        return encrypt_password;
    }
}

