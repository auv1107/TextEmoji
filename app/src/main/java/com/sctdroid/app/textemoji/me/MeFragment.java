package com.sctdroid.app.textemoji.me;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sctdroid.app.textemoji.R;
import com.sctdroid.app.textemoji.data.bean.Me;
import com.sctdroid.app.textemoji.utils.TCAgentUtils;
import com.sctdroid.app.textemoji.utils.ToastUtils;

import java.io.File;

import static android.app.Activity.RESULT_OK;

/**
 * Created by lixindong on 4/20/17.
 */

public class MeFragment extends Fragment implements MeContract.View {

    private static final int REQUEST_CODE_CROP_IMAGE = 10002;
    private MeContract.Presenter mPresenter;

    public static MeFragment newInstance() {
        return new MeFragment();
    }

    /**
     * Views here
     */
    private ImageView mAvatar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_me, container, false);

        // do initial things here
        initViews(root);
        initHeadBar(root);
        initEvent(root);

        return root;
    }

    private void initViews(View root) {
        mAvatar = (ImageView) root.findViewById(R.id.me_avatar);
    }

    private void initEvent(View root) {
        RelativeLayout avatar_container = (RelativeLayout) root.findViewById(R.id.avatar_container);
        avatar_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickAndCropImage();
            }
        });
    }

    private void initHeadBar(View root) {
        TextView title = (TextView) root.findViewById(R.id.title);
        ImageView left_option = (ImageView) root.findViewById(R.id.left_option);
        ImageView right_option = (ImageView) root.findViewById(R.id.right_option);
        title.setText(R.string.string_me);
        left_option.setVisibility(View.GONE);
        right_option.setVisibility(View.GONE);
    }

    /**
     * Methods for life circles
     */
    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_CROP_IMAGE) {
                Bitmap bitmap = data.getParcelableExtra("data");
                if (bitmap != null) {
                    mAvatar.setImageBitmap(bitmap);
                    mPresenter.updateAvatar(bitmap);
                } else {
                    ToastUtils.show(getContext(), R.string.image_load_failed, Toast.LENGTH_SHORT);
                }
            }
        }
        TCAgentUtils.UpdateAvatar(getActivity(), resultCode == RESULT_OK);
    }

    /**
     * Pick and Crop Image
     */
    private void pickAndCropImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
        intent.setType("image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", getResources().getDimensionPixelSize(R.dimen.avatar_width));
        intent.putExtra("outputY", getResources().getDimensionPixelSize(R.dimen.avatar_height));
        intent.putExtra("scale", true);
        intent.putExtra("return-data", true);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        startActivityForResult(intent, REQUEST_CODE_CROP_IMAGE);
    }

    /**
     * Methods for interface MeContract.View
     */

    @Override
    public void setPresenter(MeContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showMe(Me data) {
        if (!TextUtils.isEmpty(data.avatar)) {
            File file = new File(data.avatar);
            if (file.exists()) {
                mAvatar.setImageURI(Uri.fromFile(new File(data.avatar)));
            }
        }
    }
}
