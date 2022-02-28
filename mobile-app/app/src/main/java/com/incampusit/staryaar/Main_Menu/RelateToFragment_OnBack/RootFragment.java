package com.incampusit.staryaar.Main_Menu.RelateToFragment_OnBack;

import androidx.fragment.app.Fragment;

/*
 * Created by PANKAJ on 3/30/2018.
 */

public class RootFragment extends Fragment implements OnBackPressListener {

    @Override
    public boolean onBackPressed() {
        return new BackPressImplimentation(this).onBackPressed();
    }
}