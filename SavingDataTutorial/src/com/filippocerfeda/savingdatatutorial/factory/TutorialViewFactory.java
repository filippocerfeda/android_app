package com.filippocerfeda.savingdatatutorial.factory;

import android.view.ViewGroup;

import com.filippocerfeda.savingdatatutorial.activity.MainActivity;
import com.filippocerfeda.savingdatatutorial.view.ViewDataBase;
import com.filippocerfeda.savingdatatutorial.view.ViewDataFile;
import com.filippocerfeda.savingdatatutorial.view.ViewDataKeyValues;

public class TutorialViewFactory {
	
	public static enum TypeView {DATA_BASE, DATA_FILE, DATA_KEY_VALUE};
	
	public static ITutorialViewSaving createView(TypeView typeView, MainActivity activity, ViewGroup container) {
		switch (typeView) {
		case DATA_BASE:
        	return new ViewDataBase (activity, container);
		case DATA_FILE:
			return new ViewDataFile (activity, container);
		case DATA_KEY_VALUE:
			return new ViewDataKeyValues(activity, container);
		default:
			return null;
		}
	}

	public static ITutorialViewSaving createView(int position, MainActivity activity, ViewGroup container) {
		if (position == 0)
        	return new ViewDataBase (activity, container);
		if (position == 1)
			return new ViewDataFile (activity, container);
		if (position == 2)
			return new ViewDataKeyValues(activity, container);
		return null;
	}
}
