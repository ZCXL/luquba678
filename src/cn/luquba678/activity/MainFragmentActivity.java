package cn.luquba678.activity;

import cn.luquba678.R;
import cn.luquba678.activity.adapter.MainFragmentPagerAdapt;
import cn.luquba678.utils.ImageUtil;
import cn.luquba678.view.MainViewPager;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhuchao.bean.Version;
import com.zhuchao.connection.DownloadServiceConnection;
import com.zhuchao.function.CheckVersion;
import com.zhuchao.receiver.DownloadReceiver;
import com.zhuchao.service.DownloadService;
import com.zhuchao.view_rewrite.VersionCheckDialog;

public class MainFragmentActivity extends FragmentActivity implements OnPageChangeListener {
	public MainViewPager viewPager;
	public Fragment fragments[] = null;
	private static ImageView[] tabs = null;
	private static LinearLayout[] lintabs = null;
	private static TextView[] tvtabs = null;

	private static int currIndex = 0;// 当前页卡编号
	private FragmentManager fragmentManager;
	private int navigationLength = Resources.classes.length;

    //Update version
    public static DownloadService downloadService;
    //Download receiver
    private DownloadReceiver downloadReceiver;
    //Download connection
    private DownloadServiceConnection downloadServiceConnection;

    private long exitTime;

	private Version version;
	private CheckVersion checkVersion;
	private String versionString;

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case 1:
					if(!version.getVersionId().equals(versionString))
						new VersionCheckDialog(MainFragmentActivity.this,version.getVersionId(),version.getVersionDescription(),version.getVersionUrl()).show();
					break;
			}
		}
	};
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fragment_main);
		initViews();// 初始化控件
        initService();//init download service
		try {
			fragments = new Fragment[navigationLength];
			for (int i = 0; i < navigationLength; i++) {
				fragments[i] = (Fragment) Resources.classes[i].newInstance();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		fragmentManager = this.getSupportFragmentManager();
		viewPager = (MainViewPager) findViewById(R.id.viewPager);
		// viewPager.setSlipping(false);//设置ViewPager是否可以滑动
		viewPager.setOffscreenPageLimit(5);
		viewPager.setOnPageChangeListener(this);
		// viewPager.setAdapter(new MyPagerAdapter());
		viewPager.setAdapter(new MainFragmentPagerAdapt(fragmentManager, fragments));
		viewPager.setCurrentItem(0);// 选择起始页
		// onPageSelected(0);
		// lintabs.get(2).performClick();
	}

	/**
	 * @Title: initViews
	 * @Description: TODO初始化控件
	 */
	public void initViews() {
		tvtabs = new TextView[navigationLength];
		lintabs = new LinearLayout[navigationLength];
		tabs = new ImageView[navigationLength];
		for (int i = 0; i < navigationLength; i++) {
			tvtabs[i] = (TextView) findViewById(Resources.navigationText[i]);
			lintabs[i] = (LinearLayout) findViewById(Resources.navigationLayoutIds[i]);
			tabs[i] = (ImageView) findViewById(Resources.navigationImgViewIds[i]);
			lintabs[i].setOnClickListener(new MyOnClickListener(i));
		}
		/**
		 * check version
		 */
		checkVersion=new CheckVersion(this);
		PackageManager packageManager=getPackageManager();
		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
			versionString=packageInfo.versionName;
		} catch (PackageManager.NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		checkVersion.setOnVersionCheckListener(new CheckVersion.OnVersionCheckListener() {
			@Override
			public void getVersion(Version version) {
				MainFragmentActivity.this.version=version;
				mHandler.sendEmptyMessage(1);
			}
		});
		checkVersion.startCheck();
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		for(int i=0;i<fragments.length;i++)
			fragments[i].onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * @ClassName: MyOnPageChangeListener
	 * @Description: TODO页卡切换监听
	 * @author Collin
	 *
	 */
	@Override
	public void onPageSelected(int i) {
		int[] iconsNormal = Resources.iconsNormal;
		// 前一个图标变回去
		ImageUtil.setDraById(tabs[currIndex], iconsNormal[currIndex], this);
		tvtabs[currIndex].setTextColor(getResources().getColor(
				Resources.textcolors[0]));
		int[] ic = Resources.iconsChoosed;
		// 选择的图标变色
		ImageUtil.setDraById(tabs[i], ic[i], this);
		tvtabs[i].setTextColor(getResources().getColor(Resources.textcolors[1]));
		currIndex = i;
	}


	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	/**
	 * @ClassName: MyOnClickListener
	 * @Description: TODO头标点击监听
	 * @author Panyy
	 * @date 2013 2013年11月6日 下午2:46:08
	 *
	 */
	private class MyOnClickListener implements View.OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			//viewPager.setCurrentItem(index);
			viewPager.setCurrentItem(index,false);
		}
	}
	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK ){
            if((System.currentTimeMillis()-exitTime) >1000){
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onResume(){
        super.onResume();
        if(downloadServiceConnection!=null){
            Intent it = new Intent(MainFragmentActivity.this, DownloadService.class);
            bindService(it,downloadServiceConnection, BIND_AUTO_CREATE);
        }
        onPageSelected(viewPager.getCurrentItem());
    }

	@Override
	public void onDestroy(){
		if(downloadServiceConnection!=null)
			unbindService(downloadServiceConnection);
		if(downloadReceiver!=null){
			unregisterReceiver(downloadReceiver);
		}
		super.onDestroy();
	}
    private void initService(){
        downloadServiceConnection=new DownloadServiceConnection();
        Intent intent=new Intent(MainFragmentActivity.this,DownloadService.class);
        bindService(intent, downloadServiceConnection, BIND_AUTO_CREATE);
        startService(intent);

        IntentFilter filter=new IntentFilter();
        filter.addAction(DownloadReceiver.ACTION_UPDATE);
        filter.addAction(DownloadReceiver.ACTION_FINISHED);
        filter.addAction(DownloadReceiver.ACTION_FAILED);
        downloadReceiver=new DownloadReceiver(MainFragmentActivity.this);
        registerReceiver(downloadReceiver, filter);
    }
}
