package com.example.ninjafleet.Adapters;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.ninjafleet.MachineryTabFragment;
import com.example.ninjafleet.models.MachineryModel;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MachineryTabAdapter extends FragmentStateAdapter {
    List<String> categories;
    Map<String, List<MachineryModel.Machinery>> categorizedMachinery ;
    public MachineryTabAdapter(@NonNull FragmentActivity fragmentActivity , List<String> categories , Map<String, List<MachineryModel.Machinery>> categorizedMachinery) {
        super(fragmentActivity);
        this.categories =categories;
        this.categorizedMachinery=categorizedMachinery;

    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
         Fragment fragment = new MachineryTabFragment();
         Bundle args = new Bundle();
         // The object is just an integer.
         args.putString("title",categories.get(position));
         args.putSerializable("list", (Serializable) categorizedMachinery.get(categories.get(position)));
         args.putInt( MachineryTabFragment.ARG_OBJECT, position + 1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getItemCount() {
         return categories.size();
    }
}
