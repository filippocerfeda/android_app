package com.filippocerfeda.savingdatatutorial.slidingtabs;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.filippocerfeda.savingdatatutorial.R;
import com.filippocerfeda.savingdatatutorial.activity.MainActivity;
import com.filippocerfeda.savingdatatutorial.factory.ITutorialViewSaving;
import com.filippocerfeda.savingdatatutorial.factory.TutorialViewFactory;
import com.filippocerfeda.savingdatatutorial.factory.TutorialViewFactory.TypeView;
import com.filippocerfeda.savingdatatutorial.view.ViewDataBase;


public class SlidingTabsBasicFragment extends Fragment {


    private ViewPager viewPager;
    private SlidingTabLayout slidingTabLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sample, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        viewPager.setAdapter(new PageSavingMode());
        slidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.sliding_tabs);
        slidingTabLayout.setViewPager(viewPager);
    }

    class PageSavingMode extends PagerAdapter {
    	
    	Resources res = getResources();
    	private final String[] tabs = res.getStringArray(R.array.tabs);

        @Override
        public int getCount() {
            return tabs.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return o == view;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabs[position];
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

//        	ViewDataKeyValues vdkv = new ViewDataKeyValues((MainActivity)getActivity(), container);
//        	return vdkv.viewSavingData();
        	
//        	ViewFilesData vfd = new ViewFilesData((MainActivity)getActivity(), container);
//        	return vfd.viewSavingData();
        	ITutorialViewSaving view =  TutorialViewFactory.createView(position, 
        				(MainActivity)getActivity(), container);
        	return view.viewSavingData();
            
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }
}
