package com.example.aura.location;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DapatkanAlamatTask extends AsyncTask<Location, Void, String> {

    private Context mContext;
    private onTaskSelesai mListener;

    DapatkanAlamatTask(Context applicationContext, onTaskSelesai listener){
        mContext = applicationContext;
        mListener = listener;
    }
    @Override
    protected void onPostExecute(String alamat) {
        mListener.onTaskCompleted(alamat);
        super.onPostExecute(alamat);

    }

    @Override
    protected String doInBackground(Location... locations) {
//        return null;
        Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
        Location location = locations[0];
        List<Address> alamat = null ;
        String resultMessage = "";

        try{
            alamat = geocoder.getFromLocation(
                    location.getLatitude(),
                    location.getLongitude(),1 );
        } catch (IOException ioException){
            resultMessage = "service tidak tersedia";
            Log.e("lokasiError", resultMessage, ioException);
        } catch (IllegalArgumentException illegalArgumentException){
            resultMessage = "koordinat invalid";
            Log.e("lokasi error", resultMessage+"."+
            "Latitude = " + location.getLatitude() +
            ", Longitude = " +location.getLongitude(),
                    illegalArgumentException);
        }
            // jika alamat tidak ditemukan, tampilkan error
            if(alamat == null || alamat.size()==0){
                if(resultMessage.isEmpty()){
                    resultMessage = "alamat tidak ditemukan";
                    Log.e("lokasierror", resultMessage);
                }
            }else {
                Address address = alamat.get(0);
                ArrayList<String> barisAlamat = new ArrayList<>();

                //dapatkan baris alamat menggunakan fungsi getAddressLine dan gabungkan
                for(int i = 0; i<= address.getMaxAddressLineIndex();i++){
                    barisAlamat.add(address.getAddressLine(i));
                }

                //gabungkan line alamat dipisah baris baru
                resultMessage = TextUtils.join("\n",barisAlamat );
            }


        return resultMessage;
    }

    interface onTaskSelesai{
        void onTaskCompleted(String result);
    }

}
