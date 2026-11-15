package ru.mediahub.app;
import android.app.*; import android.os.*; import android.content.*; import android.graphics.Color; import android.view.*; import android.webkit.*; import android.widget.*; import android.net.Uri;
public class MainActivity extends Activity {
 final String[] names={"Кинопоиск","Яндекс Музыка","START"};
 final String[] urls={"https://www.kinopoisk.ru/","https://music.yandex.ru/","https://start.ru/"};
 WebView[] views=new WebView[3]; FrameLayout frame; LinearLayout tabs; int selected=0;
 int dp(int n){return (int)(getResources().getDisplayMetrics().density*n+.5f);}
 TextView button(String label,LinearLayout parent,Runnable action){TextView b=new TextView(this);b.setText(label);b.setTextColor(-1);b.setGravity(17);b.setTextSize(15);parent.addView(b,new LinearLayout.LayoutParams(0,dp(54),1));b.setOnClickListener(v->action.run());return b;}
 @Override public void onCreate(Bundle b){super.onCreate(b);getWindow().setStatusBarColor(0xff10121b);getWindow().setNavigationBarColor(0xff10121b);CookieManager.getInstance().setAcceptCookie(true);
 LinearLayout root=new LinearLayout(this);root.setOrientation(1);root.setBackgroundColor(0xff10121b);setContentView(root);
 LinearLayout top=new LinearLayout(this);root.addView(top);button("←",top,()->{if(views[selected]!=null&&views[selected].canGoBack())views[selected].goBack();});button("⌂",top,()->views[selected].loadUrl(urls[selected]));button("↻",top,()->views[selected].reload());button("↗",top,()->{String u=views[selected].getUrl();if(u!=null)startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse(u)));});
 frame=new FrameLayout(this);root.addView(frame,new LinearLayout.LayoutParams(-1,0,1));tabs=new LinearLayout(this);tabs.setBackgroundColor(0xff222635);root.addView(tabs);for(int i=0;i<3;i++){final int x=i;button(names[i],tabs,()->show(x));}show(0);
 }
 void show(int i){selected=i;frame.removeAllViews();if(views[i]==null){WebView w=new WebView(this);views[i]=w;w.setBackgroundColor(Color.BLACK);w.getSettings().setJavaScriptEnabled(true);w.getSettings().setDomStorageEnabled(true);w.getSettings().setMediaPlaybackRequiresUserGesture(false);w.getSettings().setSupportMultipleWindows(false);CookieManager.getInstance().setAcceptThirdPartyCookies(w,true);w.setWebViewClient(new WebViewClient(){@Override public boolean shouldOverrideUrlLoading(WebView view,WebResourceRequest request){Uri u=request.getUrl();String scheme=u.getScheme();if("http".equals(scheme)||"https".equals(scheme))return false;try{startActivity(new Intent(Intent.ACTION_VIEW,u));}catch(Exception e){}return true;}});w.setWebChromeClient(new WebChromeClient());w.loadUrl(urls[i]);}frame.addView(views[i]);for(int j=0;j<tabs.getChildCount();j++){((TextView)tabs.getChildAt(j)).setTextColor(j==i?0xff9a9cff:Color.WHITE);}}
 @Override public void onBackPressed(){if(views[selected]!=null&&views[selected].canGoBack())views[selected].goBack();else super.onBackPressed();}
 @Override protected void onPause(){super.onPause();CookieManager.getInstance().flush();}
}