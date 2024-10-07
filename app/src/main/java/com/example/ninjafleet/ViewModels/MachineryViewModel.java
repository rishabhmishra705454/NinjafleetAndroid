package com.example.ninjafleet.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.ninjafleet.Repositories.MachineryRepository;
import com.example.ninjafleet.models.MachineryModel;

public class MachineryViewModel extends ViewModel {
  private MachineryRepository machineryRepository;
    public MachineryViewModel() {
        machineryRepository = new MachineryRepository();
    }

   public LiveData<MachineryModel> getMachinery(int page , int limit){
        return  machineryRepository.getMachinery(page, limit);
    }
}
