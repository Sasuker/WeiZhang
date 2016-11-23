package lib.xlistview;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import lib.xlistview.XListView.IXListViewListener;


/**
 * 封装XListView的初始化
 * 
 * @author mgs
 * @date 2015-6-4 上午9:26:17
 */
public class InitXListView implements IXListViewListener{
	int pageIndex = 1;
	int pageSize = 15;
	XListView mListView;
	public InitXListView(XListView mListView){
		this.mListView = mListView;
		init();
	}
	private void init() {
		mListView.setXListViewListener(this);
		mListView.setPullLoadEnable(false);
		mListView.setPullRefreshEnable(true);		
	}
	private void onLoad() {
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd  hh:mm:ss", Locale.CHINA);
		String date = sDateFormat.format(new Date());
		mListView.stopRefresh();
		mListView.stopLoadMore();
		mListView.setRefreshTime(date);
	}
	public <T> void refreshState(List<T> datas){
		if(pageIndex == 1)
			mListView.setSelection(0);
		if (datas == null || datas.size() == 0) {
			mListView.setPullLoadEnable(false);
		} else {
			if (datas.size() < pageSize)
				mListView.setPullLoadEnable(false);
			else
				mListView.setPullLoadEnable(true);
			this.pageIndex++;
		}
		onLoad();
	}
	
	@Override
	public void onRefresh() {
		pageIndex = 1;
		loadData.onSync(pageIndex);
	}

	@Override
	public void onLoadMore() {
		loadData.onSync(pageIndex);
	}
	private LoadDataListener loadData;
	
	public void setLoadData(LoadDataListener loadData) {
		this.loadData = loadData;
	}

	public interface LoadDataListener{
		public void onSync(int pageIndex);
	}
}
