package com.zqw.mobile.grainfull.mvp.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class UploadFiles implements Parcelable {
    public UploadFiles() {
    }

    public UploadFiles(String scaleUrl, String url) {
        this.scaleUrl = scaleUrl;
        this.url = url;
    }

    /**
     * scaleUrl :
     * url :
     */

    // 服务器上传的缩略图路径
    private String scaleUrl;
    // 服务器上传的原图路径
    private String url;
    // 微信返回的media_id
    private String media_id;

    public String getScaleUrl() {
        return scaleUrl;
    }

    public void setScaleUrl(String scaleUrl) {
        this.scaleUrl = scaleUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMedia_id() {
        return media_id;
    }

    public void setMedia_id(String media_id) {
        this.media_id = media_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.scaleUrl);
        dest.writeString(this.url);
        dest.writeString(this.media_id);
    }

    protected UploadFiles(Parcel in) {
        this.scaleUrl = in.readString();
        this.url = in.readString();
        this.media_id = in.readString();
    }

    public static final Creator<UploadFiles> CREATOR = new Creator<UploadFiles>() {
        @Override
        public UploadFiles createFromParcel(Parcel source) {
            return new UploadFiles(source);
        }

        @Override
        public UploadFiles[] newArray(int size) {
            return new UploadFiles[size];
        }
    };
}
