 微信公众号
  
 ![这里写图片描述](http://upload-images.jianshu.io/upload_images/11866078-a6969884111cd3b4?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
 
[简书](https://www.jianshu.com/u/b8159d455c69)

[APKdemo_https://github.com/AnJiaoDe/Scale-Master/blob/master/app/build/outputs/apk/app-debug.apk](https://github.com/AnJiaoDe/Scale-Master/blob/master/app/build/outputs/apk/app-debug.apk)


一直好奇酷狗的皮肤预览是怎么实现的。
难道是另外写了一个一模一样的布局文件，只是宽高不一样？
感觉贼吉尔神奇啊！
![在这里插入图片描述](http://upload-images.jianshu.io/upload_images/11866078-2c67fbd8e06ca2db.gif?imageMogr2/auto-orient/strip)
![在这里插入图片描述](http://upload-images.jianshu.io/upload_images/11866078-1e39edb984d383f0?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

![在这里插入图片描述](http://upload-images.jianshu.io/upload_images/11866078-1af451b558cf4e6d?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![在这里插入图片描述](http://upload-images.jianshu.io/upload_images/11866078-26256de0602c9001?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

想想还是觉得不可能是一个个控件写上去的，一定有某种极其神奇却简单的方法。
方法就是
![在这里插入图片描述](http://upload-images.jianshu.io/upload_images/11866078-a1d55822befcf6b6.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
Android Canvas的scale，画布缩放

```
/**
     * Preconcat the current matrix with the specified scale.
     *
     * @param sx The amount to scale in X
     * @param sy The amount to scale in Y
     */
    public void scale(float sx, float sy) {
        native_scale(mNativeCanvasWrapper, sx, sy);
    }

    /**
     * Preconcat the current matrix with the specified scale.
     *
     * @param sx The amount to scale in X
     * @param sy The amount to scale in Y
     * @param px The x-coord for the pivot point (unchanged by the scale)
     * @param py The y-coord for the pivot point (unchanged by the scale)
     */
    public final void scale(float sx, float sy, float px, float py) {
        translate(px, py);
        scale(sx, sy);
        translate(-px, -py);
    }
```
**//x缩放比例，y缩放比例，px,py，缩放中心点，可以设置左顶点或者中心等顶点
        canvas.scale(scale, scale, px, py);**

## 只需要2步就能实现仿酷狗皮肤预览缩放效果：

**1.先测量期望缩放至的宽高，然后将宽高设置为被缩放View的宽高**

```
 @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));
        width_self=getMeasuredWidth();
        height_self=getMeasuredHeight();
        super.onMeasure(MeasureSpec.makeMeasureSpec(
                width_bigview, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(height_bigview, MeasureSpec.EXACTLY));
    }
```
**2.计算scale缩放比例，如果以宽为基准缩放，那就是期望缩放至的宽度/被缩放View的宽度；如果以高为基准缩放，同理**

```
 @Override
    protected void dispatchDraw(Canvas canvas) {
        if (basedOnWidthOrHeight) {
            scale = (width_self - getPaddingTop() - getPaddingBottom()) * 1f / width_bigview;

        } else {

            scale = (height_self - getPaddingTop() - getPaddingBottom()) * 1f / height_bigview;

        }
        PaintFlagsDrawFilter pfd= new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG);
        /**
         * 对canvas设置抗锯齿的滤镜，防止变化canvas引起画质降低
         */
        canvas.setDrawFilter(pfd);
        canvas.save();
        //x缩放比例，y缩放比例，px,py，缩放中心点，可以设置左顶点或者中心等顶点
        canvas.scale(scale, scale, px, py);
        super.dispatchDraw(canvas);
        canvas.restore();
    }
```

```
在这里插入代码片
```

上图：模拟器截图有点模糊，真实手机是没毛病的

![在这里插入图片描述](http://upload-images.jianshu.io/upload_images/11866078-2bd4bb02eecce0af.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
以中心点 为缩放中心
![在这里插入图片描述](http://upload-images.jianshu.io/upload_images/11866078-3aadeb90f5c296b7.gif?imageMogr2/auto-orient/strip)
以左顶点为缩放中心
![在这里插入图片描述](http://upload-images.jianshu.io/upload_images/11866078-0a1289f869ceaee1.gif?imageMogr2/auto-orient/strip)

**完整代码：**
![在这里插入图片描述](http://upload-images.jianshu.io/upload_images/11866078-62838a73eddb6f88.gif?imageMogr2/auto-orient/strip)
```
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/layout"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical">

    <ImageView
        android:id="@+id/iv"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="1"
        android:scaleType="centerInside"
        android:src="@drawable/pic" />

    <Button
        android:id="@+id/btn"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="缩放布局" />

</LinearLayout>

```

```
public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startAppcompatActivity(ScaleViewActivity.class);
            }
        });

    }

    @Override
    public void onClick(View v) {

    }
}

```

```
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/activity_scale_view"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

 
</RelativeLayout>

```

```
public class ScaleViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scale_view);

        View layout = LayoutInflater.from(this).inflate(R.layout.activity_main, null);
        ScaleFrameLayout scaleFrameLayout = new ScaleFrameLayout(this);

        scaleFrameLayout.setLayoutParams(new RelativeLayout.LayoutParams(500,1300));
        scaleFrameLayout.addView(layout);
//        scaleFrameLayout.config(false, ScreenUtils.getScreenWidth(this),ScreenUtils.getScreenHeight(this),
//                ScreenUtils.getScreenWidth(this)/2,ScreenUtils.getScreenHeight(this)/2);
        scaleFrameLayout.config(false, ScreenUtils.getScreenWidth(this),ScreenUtils.getScreenHeight(this),0,0);


        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.activity_scale_view);
        relativeLayout.addView(scaleFrameLayout);

    }
}

```

```
public class ScaleFrameLayout extends FrameLayout {
    private float scale = 1f;
    private int width_self, height_self;
    private int width_bigview = 0, height_bigview = 0;

    private boolean basedOnWidthOrHeight = true;//默认基于宽度，按比例缩放

    private Context context;

    private float px,py;//缩放中心点

    public ScaleFrameLayout(Context context) {
        super(context);
        this.context = context;
    }

    public ScaleFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }


    public void config(boolean basedOnWidthOrHeight,int width_bigview, int height_bigview,float px, float py) {
        this.basedOnWidthOrHeight=basedOnWidthOrHeight;
        this.width_bigview = width_bigview;
        this.height_bigview = height_bigview;

        if (this.width_bigview == 0) this.width_bigview = ScreenUtils.getScreenWidth(context);
        if (this.height_bigview == 0) this.height_bigview = ScreenUtils.getScreenHeight(context);

        this.px=px;
        this.py=py;
        invalidate();
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));
        width_self=getMeasuredWidth();
        height_self=getMeasuredHeight();
        super.onMeasure(MeasureSpec.makeMeasureSpec(
                width_bigview, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(height_bigview, MeasureSpec.EXACTLY));
    }


    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (basedOnWidthOrHeight) {
            scale = (width_self - getPaddingTop() - getPaddingBottom()) * 1f / width_bigview;

        } else {

            scale = (height_self - getPaddingTop() - getPaddingBottom()) * 1f / height_bigview;

        }
        PaintFlagsDrawFilter pfd= new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG);
        /**
         * 对canvas设置抗锯齿的滤镜，防止变化canvas引起画质降低
         */
        canvas.setDrawFilter(pfd);
        canvas.save();
        //x缩放比例，y缩放比例，px,py，缩放中心点，可以设置左顶点或者中心等顶点
        canvas.scale(scale, scale, px, py);
        super.dispatchDraw(canvas);
        canvas.restore();
    }

}

```
Canvas缩放还可用于广告中，缩放广告View

![在这里插入图片描述](http://upload-images.jianshu.io/upload_images/11866078-36a6d2a4144e87e5.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

![在这里插入图片描述](http://upload-images.jianshu.io/upload_images/11866078-983f930a2093434d.gif?imageMogr2/auto-orient/strip)
## 各位老铁有问题欢迎及时联系、指正、批评、撕逼

[GitHub](https://github.com/AnJiaoDe)

关注专题[Android开发常用开源库](https://www.jianshu.com/c/3ff4b3951dc5)

[简书](https://www.jianshu.com/u/b8159d455c69)

 微信公众号
 ![这里写图片描述](http://upload-images.jianshu.io/upload_images/11866078-fcfbb45175f99de0?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

QQ群
![这里写图片描述](http://upload-images.jianshu.io/upload_images/11866078-a31ff40ac6850a6d?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
