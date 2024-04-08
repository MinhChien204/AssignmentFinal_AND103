package com.example.less3;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.less3.adapter.SinhvienAdapter;
import com.example.less3.model.Sinhvien;
import com.example.less3.retrofit.ApiService;
import com.example.less3.retrofit.Config;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TrangChu extends AppCompatActivity {
    Dialog dialog;
    RecyclerView recyclerView;

    List<Sinhvien> list;

    SinhvienAdapter sinhvienAdapter;
    FloatingActionButton fab;
    ApiService apiService;
    EditText search;
    SinhvienAdapter adapter;
    ImageView anh;
    EditText edtten, edttuoi, edtmssv;
    File file;
    MultipartBody.Part multipartBody;
    Sinhvien svien;
    Uri uri;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trang_chu);

        recyclerView = findViewById(R.id.rcv_TrangChu);
        fab = findViewById(R.id.fabAddsv);
        search = findViewById(R.id.edt_search);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.ip)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);

        loadData();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add(TrangChu.this, 0, svien);
            }
        });
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String keyword = editable.toString().trim();
                searchDistributor(keyword);
            }
        });
        findViewById(R.id.btngiam).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<List<Sinhvien>> call = apiService.getGiam();
                call.enqueue(new Callback<List<Sinhvien>>() {
                    @Override
                    public void onResponse(Call<List<Sinhvien>> call, Response<List<Sinhvien>> response) {
                        if (response.isSuccessful()) {
                            list = response.body();

                            adapter = new SinhvienAdapter(list, getApplicationContext(), TrangChu.this);

                            recyclerView.setLayoutManager(new LinearLayoutManager(TrangChu.this));
                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Sinhvien>> call, Throwable t) {
                        Log.e("Main", t.getMessage());
                    }
                });
//                Toast.makeText(Home.this, "Linh dep trai", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.btntang).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<List<Sinhvien>> call = apiService.getTang();
                call.enqueue(new Callback<List<Sinhvien>>() {
                    @Override
                    public void onResponse(Call<List<Sinhvien>> call, Response<List<Sinhvien>> response) {
                        if (response.isSuccessful()) {
                            list = response.body();

                            adapter = new SinhvienAdapter(list, getApplicationContext(), TrangChu.this);

                            recyclerView.setLayoutManager(new LinearLayoutManager(TrangChu.this));
                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Sinhvien>> call, Throwable t) {
                        Log.e("Main", t.getMessage());
                    }
                });
//                Toast.makeText(Home.this, "Linh dep trai", Toast.LENGTH_SHORT).show();
            }
        });

    }

    void loadData() {
        Call<List<Sinhvien>> call = apiService.getSinhviens();

        call.enqueue(new Callback<List<Sinhvien>>() {
            @Override
            public void onResponse(Call<List<Sinhvien>> call, Response<List<Sinhvien>> response) {
                if (response.isSuccessful()) {
                    list = response.body();
                    Log.d(TAG, "onResponse: "+list);
                    sinhvienAdapter = new SinhvienAdapter(list, TrangChu.this, TrangChu.this);
                    recyclerView.setLayoutManager(new LinearLayoutManager(TrangChu.this));
                    recyclerView.setAdapter(sinhvienAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<Sinhvien>> call, Throwable t) {
                Log.d("111","lỗi:" + t.getMessage());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uri = data.getData();
            anh.setImageURI(uri);
        }
    }

    public void add(Context context, int type, Sinhvien sv) {
        Dialog dialog = new Dialog(TrangChu.this);
        dialog.setContentView(R.layout.dialog_updatesv);

        edtten = dialog.findViewById(R.id.upName);
        edttuoi = dialog.findViewById(R.id.upAge);
        edtmssv = dialog.findViewById(R.id.upMSSV);
        anh = dialog.findViewById(R.id.btn_upload);

        anh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });

        if (type != 0) {
            edtten.setText(sv.getName());
            edttuoi.setText(sv.getTuoi() + "");
            edtmssv.setText(sv.getMssv());
            Glide.with(context)
                    .load(sv.getImage())
                    .thumbnail(Glide.with(context).load(R.drawable.img_10))
                    .into(anh);
            Log.d(TAG, "them: " + sv.getImage());
        }
        dialog.findViewById(R.id.btnSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, RequestBody> mapRequestBody = new HashMap<>();
                String _ten = edtten.getText().toString();
                String _tuoi = edttuoi.getText().toString();
                String _mssv = edtmssv.getText().toString();

                if (file != null) {
                    RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
                    multipartBody = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
                } else {
                    multipartBody = null;
                }

                if (_ten.length() == 0 || _tuoi.length() == 0 || _mssv.length() == 0) {
                    Toast.makeText(context, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    Double _age = Double.parseDouble(_tuoi);

                    mapRequestBody.put("name", getRequestBody(_ten));
                    mapRequestBody.put("tuoi", getRequestBody(String.valueOf(_tuoi)));
                    mapRequestBody.put("mssv", getRequestBody(_mssv));

                    if (_age > 0) {
                        if (type == 0) {
                            Call<Sinhvien> call = apiService.AddSinhViens(mapRequestBody, multipartBody);
                            call.enqueue(new Callback<Sinhvien>() {
                                @Override
                                public void onResponse(Call<Sinhvien> call, Response<Sinhvien> response) {
                                    if (response.isSuccessful()) {
                                        loadData();
                                        Toast.makeText(TrangChu.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    } else {
                                        Toast.makeText(TrangChu.this, "Thêm thất bại", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Sinhvien> call, Throwable t) {
                                    Log.e("Home", "Call failed: " + t.toString());
                                    Toast.makeText(TrangChu.this, "Đã xảy ra lỗi khi thêm dữ liệu", Toast.LENGTH_SHORT).show();
                                }

                            });
                        } else {
                            if (multipartBody != null) {
                                Call<Sinhvien> call = apiService.updateSinhViens(mapRequestBody, sv.get_id(), multipartBody);
                                call.enqueue(new Callback<Sinhvien>() {
                                    @Override
                                    public void onResponse(Call<Sinhvien> call, Response<Sinhvien> response) {
                                        if (response.isSuccessful()) {
                                            loadData();
                                            Toast.makeText(TrangChu.this, "Sửa thành công", Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                        } else {
                                            Toast.makeText(TrangChu.this, "Sửa thất bại", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Sinhvien> call, Throwable t) {
                                        Log.e("Home", "Call failed: " + t.toString());
                                        Toast.makeText(TrangChu.this, "Đã xảy ra lỗi khi sửa dữ liệu", Toast.LENGTH_SHORT).show();
                                    }

                                });
                            } else {
                                Call<Sinhvien> call = apiService.updateNoImage(mapRequestBody, sv.get_id());
                                call.enqueue(new Callback<Sinhvien>() {
                                    @Override
                                    public void onResponse(Call<Sinhvien> call, Response<Sinhvien> response) {
                                        if (response.isSuccessful()) {
                                            loadData();
                                            Toast.makeText(TrangChu.this, "Sửa thành công no image", Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                        } else {
                                            Toast.makeText(TrangChu.this, "Sửa thất bại no image", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Sinhvien> call, Throwable t) {
                                        Log.e("Home", "Call failed: " + t.toString());
                                        Toast.makeText(TrangChu.this, "Đã xảy ra lỗi khi sửa dữ liệu", Toast.LENGTH_SHORT).show();
                                    }

                                });
                            }
                        }
                    } else {
                        Toast.makeText(context, "Giá phải lớn hơn 0", Toast.LENGTH_SHORT).show();
                    }

                } catch (NumberFormatException e) {
                    Toast.makeText(context, "Gía phải là số", Toast.LENGTH_SHORT).show();
                }

            }
        });

        dialog.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    private void chooseImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        getImage.launch(intent);

    }

    ActivityResultLauncher<Intent> getImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult o) {
                    if (o.getResultCode() == Activity.RESULT_OK) {
                        Intent data = o.getData();
                        Uri imageUri = data.getData();

                        Log.d("RegisterActivity", imageUri.toString());

                        file = createFileFormUri(imageUri, "image");

                        Glide.with(anh)
                                .load(imageUri)
                                .centerCrop()
                                .circleCrop()
                                .into(anh);
                    }
                }
            });

    private File createFileFormUri(Uri path, String name) {
        File _file = new File(TrangChu.this.getCacheDir(), name + ".png");
        try {
            InputStream in = TrangChu.this.getContentResolver().openInputStream(path);
            OutputStream out = new FileOutputStream(_file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            in.close();
            return _file;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private RequestBody getRequestBody(String value) {
        return RequestBody.create(MediaType.parse("multipart/form-data"), value);
    }


    public void xoa(String id) {
        Call<Void> call = apiService.deleteSinhViens(id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    loadData();
                    Toast.makeText(TrangChu.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(TrangChu.this, "Xóa fail", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Xảy ra lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchDistributor(String keyword) {
        Call<List<Sinhvien>> call = apiService.searchCay(keyword);
        call.enqueue(new Callback<List<Sinhvien>>() {
            @Override
            public void onResponse(Call<List<Sinhvien>> call, Response<List<Sinhvien>> response) {
                if (response.isSuccessful()) {
                    list = response.body();

                    adapter = new SinhvienAdapter(list, getApplicationContext(), TrangChu.this);
                    recyclerView.setLayoutManager(new LinearLayoutManager(TrangChu.this));
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Sinhvien>> call, Throwable t) {
                Log.e("Search", "Search failed: " + t.toString());
                Toast.makeText(TrangChu.this, "Đã xảy ra lỗi kh tìm kiếm", Toast.LENGTH_SHORT).show();
            }
        });
    }

}