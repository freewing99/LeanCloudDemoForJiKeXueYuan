package com.leancloud.geek;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.avos.avoscloud.AVCloudQueryResult;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.CloudQueryCallback;
import com.avos.avoscloud.DeleteCallback;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    final AVObject Manchester_United=new AVObject("Team");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //把曼联加到我们的 Team 表里面去，开启英超之旅~~~
        //create MU
        Button  btn_saveObj= (Button)findViewById(R.id.btn_saveObj);
        btn_saveObj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Manchester_United.put("name","曼联");//设置属性 “name” 的值为 “曼联”
                Manchester_United.put("nickName", "红魔");
                Manchester_United.put("rank",4);//我大曼联现在排名虽然只有第4！
                Manchester_United.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        Toast.makeText(getApplicationContext(), "曼联，加油！",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        //update MU
        Button btn_update=(Button)findViewById(R.id.update_object);
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Manchester_United.put("rank",1);
                Manchester_United.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        Toast.makeText(getApplicationContext(), "曼联现在排名是:"+Manchester_United.getInt("rank")+",好开心！",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        //delete MU
        Button btn_delete=(Button)findViewById(R.id.btn_delete);
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Manchester_United.deleteInBackground(new DeleteCallback() {
                    @Override
                    public void done(AVException e) {
                        Toast.makeText(getApplicationContext(), "再见，曼联！",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        //set a city
        Button btn_setCity=(Button)findViewById(R.id.btn_setCity);
        btn_setCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AVObject city=new AVObject("City");//英格兰有很多大都市
                city.put("name","Trafford, Greater Manchester");//但是跟曼城不一样，曼联的主场叫做老特拉福德，在大曼城斯特郡
                city.put("zipCode","M17 8AA");
                city.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        Manchester_United.put("located",city);
                        Manchester_United.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(AVException e) {
                                Toast.makeText(getApplicationContext(), "曼彻斯特欢迎你！",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });

        //fetch MU
        Button btn_fetch=(Button)findViewById(R.id.btn_fetch);
        btn_fetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AVQuery<AVObject> mu_Query=new  AVQuery<AVObject>("Team");
                mu_Query.whereEqualTo("name","曼联");//查找名字叫曼联的球队
                mu_Query.findInBackground(new FindCallback<AVObject>() {
                    @Override
                    public void done(List<AVObject> avObjects, AVException e) {
                        if(avObjects.size() > 0)
                        {
                            Manchester_United.setObjectId(avObjects.get(0).getObjectId());//默认就找到第一个即可，只做Demo，算法不做参考。
                            Manchester_United.fetchInBackground(new GetCallback<AVObject>() {
                                @Override
                                public void done(AVObject avObject, AVException e) {
                                    Toast.makeText(getApplicationContext(), "曼联的信息加载到本地完毕！",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                    }
                });
            }
        });

        //Teams located in London
        Button btn_addTeamsToLondon=(Button)findViewById(R.id.btn_addTeamsToLondon);
        btn_addTeamsToLondon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AVObject London=new AVObject("City");//伦敦的俱乐部那真是数不清……
                London.put("name","London");

                AVObject Arsenal=new AVObject("Team");//争四狂魔阿森纳
                Arsenal.put("zipCode","N7 7AJ");
                Arsenal.put("name","阿森纳");
                Arsenal.put("nickName","枪手");
                Arsenal.put("rank",6);

                AVObject Chelsea=new AVObject("Team");//有钱任性魔力鸟
                Chelsea.put("zipCode","SW6 1HS");
                Chelsea.put("name","切尔西");
                Chelsea.put("nickName", "蓝军");
                Chelsea.put("rank",1);

                AVObject Hotspur=new AVObject("Team");//枪手宿敌大热刺
                Hotspur.put("zipCode","N17 0AP");
                Hotspur.put("name","托特纳姆热刺");
                Hotspur.put("nickName", "刺");
                Hotspur.put("rank",5);

                final List<AVObject> teamsInLondon = new ArrayList<AVObject>();
                teamsInLondon.add(Arsenal);
                teamsInLondon.add(Chelsea);
                teamsInLondon.add(Hotspur);


                AVObject.saveAllInBackground(teamsInLondon,new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        London.put("clubs",teamsInLondon);

                        London.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(AVException e) {
                                Toast.makeText(getApplicationContext(), "伦敦欢迎你！",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });

        //Query MU by Id
        Button btn_queryById=(Button)findViewById(R.id.btn_queryById);
        final String MU_id="54c63936e4b068d1ee4353fe";//运行前请输入
        btn_queryById.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AVQuery<AVObject> mu_Query_by_Id=new  AVQuery<AVObject>("Team");
                mu_Query_by_Id.getInBackground(MU_id,new GetCallback<AVObject>() {
                    @Override
                    public void done(AVObject avObject, AVException e) {
                        Toast.makeText(getApplicationContext(), avObject.getString("name")+"已经就绪！",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        //Find MU by name
        Button btn_findByName=(Button)findViewById(R.id.btn_findByName);
        btn_findByName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AVQuery<AVObject> mu_query_by_name=new  AVQuery<AVObject>("Team");
                mu_query_by_name.whereEqualTo("name","曼联");
                mu_query_by_name.findInBackground(new FindCallback<AVObject>() {
                    @Override
                    public void done(List<AVObject> avObjects, AVException e) {
                        if(avObjects.size()> 0)
                        {
                           AVObject mu_findByName=  avObjects.get(0);
                            Toast.makeText(getApplicationContext(), mu_findByName.getString("nickName")+"被找到！",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        //Query MU by CQL
        Button btn_cql=(Button)findViewById(R.id.btn_cql);
        btn_cql.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AVQuery.doCloudQueryInBackground("select * from Team where name='曼联'",new CloudQueryCallback<AVCloudQueryResult>(){

                    @Override
                    public void done(AVCloudQueryResult result, AVException cqlException) {
                        if(cqlException==null){

                            AVObject  mu=result.getResults().get(0);//这里是你查询到的结果
                            Toast.makeText(getApplicationContext(), mu.getString("name")+"，加油！",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
