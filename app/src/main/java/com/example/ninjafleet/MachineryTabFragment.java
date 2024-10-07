package com.example.ninjafleet;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ninjafleet.Adapters.MachineryRVAdapter;
import com.example.ninjafleet.databinding.FragmentMachineryTabBinding;
import com.example.ninjafleet.models.MachineryModel;

import java.util.List;

public class MachineryTabFragment extends Fragment {

   private FragmentMachineryTabBinding binding;
    public static final String ARG_OBJECT = "object";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMachineryTabBinding.inflate(getLayoutInflater(), container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

//      binding.text.setText(args.getString("title"));
        List<MachineryModel.Machinery> machineryList = (List<MachineryModel.Machinery>) getArguments().getSerializable("list");
        for (int i =0;i<machineryList.size(); i++){
            Log.d("Machinery", machineryList.get(i).getMachineryName());
        }

        MachineryRVAdapter adapter = new MachineryRVAdapter(getContext(), machineryList);
        binding.machineryRV.setHasFixedSize(true);
        binding.machineryRV.setLayoutManager(new GridLayoutManager(getContext(),2));
        binding.machineryRV.setAdapter(adapter);



    }
}