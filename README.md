# SJ_APP

    private ViewPagerTab homeTab;
    private LinearLayout mainView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    public void initView(){
        mainView=findViewById(R.id.main_view);//在该视图下添加

        TabConfig tabConfig =new TabConfig();//配置信息
        tabConfig.setSpeed(1500);//翻页时间 速度
        tabConfig.setStyle(0);//翻页风格  封装来源 https://github.com/AndroidMsky/ViewPagerAnimation
        tabConfig.setTabHeight(50); //底部按钮高度
        tabConfig.setTextSize(10); //底部文字大小

        List<PageView> list=new ArrayList<>();//视图列表
        //参数分别为 layout布局文件 不能为null，按钮未选中的图片 不能为null，按钮选中的图片 可为null，按钮文字 可为null ，按钮布局margin 可为null 
        PageView news=new PageView(R.layout.tab_news_layout,R.drawable.news,R.drawable.news_1,"动态",new LayoutBounds(0,10));
        list.add(news);
        PageView home=new PageView(R.layout.tab_home_layout,R.drawable.home,R.drawable.home_1,null,new LayoutBounds(0,10));
        list.add(home);
        PageView my=new PageView(R.layout.tab_my_layout,R.drawable.my,R.drawable.my_1,"我的",new LayoutBounds(0,10));
        list.add(my);
        //创建即初始化
        homeTab=new ViewPagerTab(this,mainView,list, tabConfig);

        //初始化页面（第几个页面，回调）
       homeTab.initView(0, new InitView() {
            @Override
            public void init(PageViewProtect pageViewProtect) {
                TextView textView=pageViewProtect.getContentView().findViewById(R.id.test);
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        
                    }
                });
            }
        });
       
       //切换 监听 ps：监听后 需要自行控制按钮效果
        homeTab.setOnHomeTabChangeListener(new OnHomeTabChangeListener() {

            @Override
            public void onPageScrolled(int i, float v, int i1) {
                
            }

            @Override
            public void onPageSelected(int i, PageViewProtect pageViewProtect) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }

            @Override
            public void onTabClick(int i, LinearLayout layout, ImageView icon, TextView txt) {

            }
        });

    }
