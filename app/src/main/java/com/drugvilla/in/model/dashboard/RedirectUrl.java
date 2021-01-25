package com.drugvilla.in.model.dashboard;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RedirectUrl {

    @SerializedName("faq_url")
    @Expose
    private String faqUrl;
    @SerializedName("about_us_url")
    @Expose
    private String aboutUsUrl;
    @SerializedName("privacy_policy_url")
    @Expose
    private String privacyPolicyUrl;
    @SerializedName("terms_conditions_url")
    @Expose
    private String termsConditionsUrl;
    @SerializedName("dv_twitter")
    @Expose
    private String dvTwitter;
    @SerializedName("dv_facebook")
    @Expose
    private String dvFacebook;
    @SerializedName("dv_linkedin")
    @Expose
    private String dvLinkedin;
    @SerializedName("dv_youtube")
    @Expose
    private String dvYoutube;

    public String getFaqUrl() {
        return faqUrl;
    }

    public void setFaqUrl(String faqUrl) {
        this.faqUrl = faqUrl;
    }

    public String getAboutUsUrl() {
        return aboutUsUrl;
    }

    public void setAboutUsUrl(String aboutUsUrl) {
        this.aboutUsUrl = aboutUsUrl;
    }

    public String getPrivacyPolicyUrl() {
        return privacyPolicyUrl;
    }

    public void setPrivacyPolicyUrl(String privacyPolicyUrl) {
        this.privacyPolicyUrl = privacyPolicyUrl;
    }

    public String getTermsConditionsUrl() {
        return termsConditionsUrl;
    }

    public void setTermsConditionsUrl(String termsConditionsUrl) {
        this.termsConditionsUrl = termsConditionsUrl;
    }

    public String getDvTwitter() {
        return dvTwitter;
    }

    public void setDvTwitter(String dvTwitter) {
        this.dvTwitter = dvTwitter;
    }

    public String getDvFacebook() {
        return dvFacebook;
    }

    public void setDvFacebook(String dvFacebook) {
        this.dvFacebook = dvFacebook;
    }

    public String getDvLinkedin() {
        return dvLinkedin;
    }

    public void setDvLinkedin(String dvLinkedin) {
        this.dvLinkedin = dvLinkedin;
    }

    public String getDvYoutube() {
        return dvYoutube;
    }

    public void setDvYoutube(String dvYoutube) {
        this.dvYoutube = dvYoutube;
    }

}
