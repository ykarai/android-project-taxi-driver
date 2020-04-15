package com.example.yosk7.drivertaxiapp.model.datasource;

public class Utils {
//    public static void showToast(Context mContext, String message){
//        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
//    }


    public interface Action<T> {
        void onSuccess(T obj);

        void onFailure(Exception exception);

        void onProgress(String status, double percent);
    }

    public interface NotifyDataChange<T> {
        void OnDataChanged(T obj);

        void onFailure(Exception exception);
    }


    public interface NotifyEndLoadinData<T> {

        void onEnd(T obj);


    }

}
