package com.example.finalproject.call_us_or_find_us;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.finalproject.R;
import com.example.finalproject.databinding.FragmentCallUsOrFindUsBinding;

public class Call_us_or_Find_usFragment extends Fragment {

    private FragmentCallUsOrFindUsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCallUsOrFindUsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Button callBtn = root.findViewById(R.id.callBtn);
        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dialIntent = new Intent(Intent.ACTION_DIAL);
                dialIntent.setData(Uri.parse("tel:0599000000"));
                startActivity(dialIntent);
            }
        });

        Button locationBtn = root.findViewById(R.id.locationBtn);
        locationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mapsIntent = new Intent(Intent.ACTION_VIEW);
                mapsIntent.setData(Uri.parse("geo:31.961013,35.190483"));
                startActivity(mapsIntent);
            }
        });

        Button emailBtn = root.findViewById(R.id.emailBtn);
        emailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto:AdvancePizza@Pizza.com"));
                startActivity(emailIntent);
            }
        });
        ImageView arrow1 = root.findViewById(R.id.arrow1);
        ImageView arrow2 = root.findViewById(R.id.arrow2);

        Animation arrowAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.arrow_tween_animation);

        arrow1.startAnimation(arrowAnimation);
        arrow2.startAnimation(arrowAnimation);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
