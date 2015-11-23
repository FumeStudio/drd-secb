package com.secb.android.controller.manager;


import com.secb.android.model.Paging;

import java.util.ArrayList;
import java.util.List;

public class PagingManager {
    public static int getLastPageNumber(ArrayList<? extends Paging> items) {
        return (items != null && items.size() > 0) ? items.get(items.size()-1).pageNumber : -1;
    }
    public static List<? extends Paging> updatePaging(List<? extends Paging> items, int pageIndex) {
        if(items != null) {
            for(Paging item : items)
                item.pageNumber = pageIndex;
        }
        return items;
    }
}
