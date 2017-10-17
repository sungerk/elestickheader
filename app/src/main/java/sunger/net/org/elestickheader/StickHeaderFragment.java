package sunger.net.org.elestickheader;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunger on 2017/10/15.
 */

public class StickHeaderFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView recyclerViewFilter;
    private View layoutStickHeader;
    private View layoutFilter;
    private ExpandableLayout expandableLayout;
    private CommonAdapter commonAdapter;
    private LinearLayoutManager linearLayoutManager;
    private int top = -1;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stickheader, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerViewFilter = (RecyclerView) view.findViewById(R.id.recyclerView_filter);
        layoutStickHeader = view.findViewById(R.id.layout_stick_header);
        layoutFilter = view.findViewById(R.id.layout_filter);
        expandableLayout = (ExpandableLayout) view.findViewById(R.id.expandableLayout);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerViewFilter.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setLayoutManager(linearLayoutManager);
        commonAdapter = new CommonAdapter(R.layout.item_common_text);
        List<String> data = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            data.add("item" + i);
        }
        View header1 = LayoutInflater.from(getContext()).inflate(R.layout.view_header_banner, recyclerView, false);
        commonAdapter.addHeaderView(header1);


        View header2 = LayoutInflater.from(getContext()).inflate(R.layout.view_header_banner2, recyclerView, false);
        commonAdapter.addHeaderView(header2);

        final View header3 = LayoutInflater.from(getContext()).inflate(R.layout.view_header3, recyclerView, false);
        initStickView(header3);
        commonAdapter.addHeaderView(header3);
        header3.setBackgroundColor(Color.BLUE);

        commonAdapter.addData(data);
        recyclerView.setAdapter(commonAdapter);
        layoutFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandableLayout.collapse();
            }
        });
        expandableLayout.setOnExpansionUpdateListener(new ExpandableLayout.OnExpansionUpdateListener() {
            @Override
            public void onExpansionUpdate(float expansionFraction, int state) {
                if (state == 0) {
                    layoutFilter.setVisibility(View.GONE);
                }
            }
        });

        List<String> dataFilter = new ArrayList<>();
        dataFilter.add("综合排序");
        dataFilter.add("销量最高");
        dataFilter.add("起送价最低");
        dataFilter.add("配送最快");
        dataFilter.add("配送费最低");
        dataFilter.add("人均从低到高");
        dataFilter.add("人均从高到低");

        CommonAdapter filterAdapter = new CommonAdapter(R.layout.item_filter);
        filterAdapter.addData(dataFilter);
        recyclerViewFilter.setAdapter(filterAdapter);

    }


    private void initStickView(final View view) {
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                top = view.getTop();
                addHeaderFilterClickListener(view);
                addScrollListener();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });
    }

    private void addHeaderFilterClickListener(final View view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.smoothScrollBy(0, view.getTop() - mScrollY);
                showFilter();
            }
        });
    }


    private void showFilter() {
        layoutFilter.postDelayed(new Runnable() {
            @Override
            public void run() {
                layoutFilter.setVisibility(View.VISIBLE);
                expandableLayout.expand();
            }
        }, 300);


    }

    private int mScrollY = 0;


    private void addScrollListener() {

        layoutStickHeader.setTranslationY(top);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy == 0) return;
                mScrollY += dy;
                int translationY = top - mScrollY;
                if (translationY < 0) translationY = 0;
                layoutStickHeader.setTranslationY(translationY);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }

}
