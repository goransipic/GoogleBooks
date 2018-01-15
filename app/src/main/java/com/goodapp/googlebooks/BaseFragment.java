package com.goodapp.googlebooks;

import android.support.v4.app.Fragment;

/**
 * Created by gsipic on 15/01/2018.
 */

public class BaseFragment extends Fragment {

    /**
     * Could handle back press.
     * @return true if back press was handled
     */
    public boolean onBackPressed() {
        return false;
    }

}
