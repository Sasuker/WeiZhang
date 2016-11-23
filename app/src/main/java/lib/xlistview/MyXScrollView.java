package lib.xlistview;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;

import com.example.administrator.take_two.R;
import com.example.administrator.take_two.Util.ScreenUtil;

/**
 *
 * @author markmjw
 * @date 2013-10-08
 */
public class MyXScrollView extends ScrollView implements OnScrollListener {
//    private static final String TAG = "XScrollView";

    private final static int SCROLL_BACK_HEADER = 0;

    private final static int SCROLL_DURATION = 400;


    // support iOS like pull
    private final static float OFFSET_RADIO = 1.8f;

    private float mLastY = -1;

    // used for scroll back
    private Scroller mScroller;
    // user's scroll listener
    private OnScrollListener mScrollListener;
    // for mScroller, scroll back from header or footer.
    private int mScrollBack;

    // the interface to trigger refreshBannerView and load more.
    private IXScrollViewListener mListener;

    private LinearLayout mContentLayout;

    private XHeaderView mHeader;
    // header view content, use it to calculate the Header's height. And hide it when disable pull refreshBannerView.
    private RelativeLayout mHeaderContent;
    private TextView mHeaderTime;
    private int mHeaderHeight;

    private boolean mEnablePullRefresh = true;
    private boolean mPullRefreshing = false;

    private XFooterView mFooterView;

    Context context;
    public MyXScrollView(Context context) {
        super(context);
        this.context = context;
    }

    public MyXScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public MyXScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
    }
    //添加一个默认的空白区域，以防止内容布局太小导致的不可下拉刷新
    LinearLayout blankSpace;
    int screenH = 0;
    /**
     * onFinishInflate() 当View中所有的子控件 均被映射成xml后触发
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        screenH =  ScreenUtil.getScreenSize(context, null)[1];
//    private void initWithContext(Context context) {
        mContentLayout = (LinearLayout) findViewById(R.id.content_layout);

        mScroller = new Scroller(context, new DecelerateInterpolator());
        // XScrollView need the scroll event, and it will dispatch the event to user's listener (as a proxy).
        this.setOnScrollListener(this);

        // init header view
        mHeader = new XHeaderView(context);
        mHeaderContent = (RelativeLayout) mHeader.findViewById(R.id.header_content);
        mHeaderTime = (TextView) mHeader.findViewById(R.id.header_hint_time);
        LinearLayout headerLayout = (LinearLayout)findViewById(R.id.header_layout);
        headerLayout.addView(mHeader);

        // init header height
        ViewTreeObserver observer = mHeader.getViewTreeObserver();
        if (null != observer) {
            observer.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                @SuppressWarnings("deprecation")
                @Override
                public void onGlobalLayout() {
                    mHeaderHeight = mHeaderContent.getHeight();
                    ViewTreeObserver observer = getViewTreeObserver();
                    if (null != observer) {
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                            observer.removeGlobalOnLayoutListener(this);
                        } else {
                            observer.removeOnGlobalLayoutListener(this);
                        }
                    }
                }
            });
        }
        blankSpace = new LinearLayout(context);
        LinearLayout.LayoutParams blankSpaceParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,0);
        blankSpace.setLayoutParams(blankSpaceParams);
        mContentLayout.addView(blankSpace);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        checkAndAddBlankBlock();//检查高度
        super.onDraw(canvas);
    }

    /**
     * Enable or disable pull down refreshBannerView feature.
     *
     * @param enable
     */
    public void setPullRefreshEnable(boolean enable) {
        mEnablePullRefresh = enable;
        // disable, hide the content
        mHeaderContent.setVisibility(enable ? View.VISIBLE : View.INVISIBLE);
    }


    /**
     * Stop refreshBannerView, reset header view.
     */
    public void stopRefresh() {
        if (mPullRefreshing) {
            mPullRefreshing = false;
            resetHeaderHeight();
        }
    }


    /**
     * Set last refreshBannerView time
     *
     * @param time
     */
    public void setRefreshTime(String time) {
        mHeaderTime.setText(time);
    }

    /**
     * Set listener.
     *
     * @param listener
     */
    public void setIXScrollViewListener(IXScrollViewListener listener) {
        mListener = listener;
    }


    private void invokeOnScrolling() {
        if (mScrollListener instanceof OnXScrollListener) {
            OnXScrollListener l = (OnXScrollListener) mScrollListener;
            l.onXScrolling(this);
        }
    }

    private void updateHeaderHeight(float delta) {

        mHeader.setVisibleHeight((int) delta + mHeader.getVisibleHeight());

        if (mEnablePullRefresh && !mPullRefreshing) {
            // update the arrow image unrefreshing
            if (mHeader.getVisibleHeight() > mHeaderHeight) {
                mHeader.setState(XHeaderView.STATE_READY);
            } else {
                mHeader.setState(XHeaderView.STATE_NORMAL);
            }
        }

        // scroll to top each time
        post(new Runnable() {
            @Override
            public void run() {
                MyXScrollView.this.fullScroll(ScrollView.FOCUS_UP);
            }
        });
    }

    private void resetHeaderHeight() {
        int height = mHeader.getVisibleHeight();
        if (height == 0) return;

        // refreshing and header isn't shown fully. do nothing.
        if (mPullRefreshing && height <= mHeaderHeight) return;

        // default: scroll back to dismiss header.
        int finalHeight = 0;
        // is refreshing, just scroll back to show all the header.
        if (mPullRefreshing && height > mHeaderHeight) {
            finalHeight = mHeaderHeight;
        }

        mScrollBack = SCROLL_BACK_HEADER;
        mScroller.startScroll(0, height, 0, finalHeight - height, SCROLL_DURATION);
        invalidate();
    }

    private float downY = 0;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                downY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                float y = ev.getRawY();
                if(Math.abs(downY - y)>10){
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }

        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        try{
            if (mLastY == -1) {
                mLastY = ev.getRawY();
            }

            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
    //                checkAndAddBlankBlock();

                    mLastY = ev.getRawY();
                    break;

                case MotionEvent.ACTION_MOVE:
                    //不让继续执行了
                    if(mPullRefreshing){
                        break;
                    }

                    final float deltaY = ev.getRawY() - mLastY;
                    mLastY = ev.getRawY();
                    if (isTop() && (mHeader.getVisibleHeight() > 0 || deltaY > 0)) {
                        updateHeaderHeight(deltaY / OFFSET_RADIO);
                        invokeOnScrolling();
                    }
                    break;
                default:
                    // reset
                    mLastY = -1;
                    resetHeaderOrBottom();
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
            Log.e("MyXScrollView", " onTouchEvent error");
        }
        return super.onTouchEvent(ev);
    }

//    /**判断内容区域高度，并添加适当的空白区域*/
//    public void checkAndAddBlankBlock(){
//        if (mContentLayout.getHeight() <= screenH) {
//            ViewGroup.LayoutParams lps = blankSpace.getLayoutParams();
//            lps.height = screenH - mContentLayout.getHeight() + 5;//这里不知道是为什么，总是太高
//            lps.height = allMeasure() - DensityUtil.dip2px(context,44);
//            blankSpace.setLayoutParams(lps);
//            blankSpace.requestLayout();
//            mContentLayout.requestLayout();
//        }
//        invalidate();
//    }


    /**
     * 测量ContentLayout内部子View的所有高度
     * @return
     */
    public int allMeasure(){
        int childCount = mContentLayout.getChildCount();
        int contentHeight = 0;
        for(int i=0;i<childCount-1;i++){//最后一个不要
            contentHeight = contentHeight + mContentLayout.getChildAt(i).getHeight();
        }
        return screenH - contentHeight + 2;
    }

    private void resetHeaderOrBottom() {
        if (isTop()) {
            if (mEnablePullRefresh && mHeader.getVisibleHeight() > mHeaderHeight) {
                mPullRefreshing = true;
                mHeader.setState(XHeaderView.STATE_REFRESHING);
                refresh();
            }
            resetHeaderHeight();

        }
    }

    private boolean isTop() {
        return getScrollY() <= 0 || mHeader.getVisibleHeight() > mHeaderHeight;
    }


    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            mHeader.setVisibleHeight(mScroller.getCurrY());
            postInvalidate();
            invokeOnScrolling();
        }
        super.computeScroll();
    }

    public void setOnScrollListener(OnScrollListener l) {
        mScrollListener = l;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (mScrollListener != null) {
            mScrollListener.onScrollStateChanged(view, scrollState);
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                         int totalItemCount) {
        Log.e("here_sroll_y", getScrollY() + "");
        if (mScrollListener != null) {
            mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
    }

    private void refresh() {
        if (mEnablePullRefresh && null != mListener) {
            mListener.onRefresh();
        }
    }

    public interface OnXScrollListener extends OnScrollListener {
        public void onXScrolling(View view);
    }

    public interface IXScrollViewListener {
        public void onRefresh();
    }
}
