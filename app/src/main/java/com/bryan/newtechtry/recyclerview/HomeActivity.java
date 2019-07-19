package com.bryan.newtechtry.recyclerview;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bryan.newtechtry.R;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity
{

	private RecyclerView mRecyclerView;
	private List<String> mDatas;
	private HomeAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_single_recyclerview);

		initData();

		mRecyclerView = (RecyclerView) findViewById(R.id.id_recyclerview);
		mAdapter = new HomeAdapter(this, mDatas);

		mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(4,
				StaggeredGridLayoutManager.VERTICAL));

		
		
		mRecyclerView.setAdapter(mAdapter);
		mRecyclerView.addItemDecoration(new DividerGridItemDecoration(this));
		
		// 设置item动画
		mRecyclerView.setItemAnimator(new DefaultItemAnimator());

		initEvent();

	}


	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	private void initEvent()
	{
		mAdapter.setOnItemClickLitener(new HomeAdapter.OnItemClickLitener()
		{
			@Override
			public void onItemClick(View view, int position)
			{
				Toast.makeText(HomeActivity.this, position + " click",
						Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onItemLongClick(View view, int position)
			{
				Toast.makeText(HomeActivity.this, position + " long click",
						Toast.LENGTH_SHORT).show();
			}
		});
	}

	protected void initData()
	{
		mDatas = new ArrayList<String>();
		for (int i = 'A'; i < 'z'; i++)
		{
			mDatas.add("" + (char) i);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case R.id.id_action_add:
			mAdapter.addData(1);
			//mRecyclerView.smoothScrollToPosition(0);
			break;
		case R.id.id_action_delete:
			mAdapter.removeData(1);
			break;
		case R.id.id_action_gridview:
			mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
			break;
		case R.id.id_action_listview:
			mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
			break;
		case R.id.id_action_horizonlistview:
			mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
			break;
		case R.id.id_action_horizontalGridView:
			mRecyclerView.setLayoutManager(new GridLayoutManager(this,4,
					GridLayoutManager.HORIZONTAL,false));
			break;

		case R.id.id_action_staggeredgridview:{
			Intent intent = new Intent(this , StaggeredGridLayoutActivity.class);
			startActivity(intent);
			break;
		}
			
		case R.id.id_action_complex:{
			Intent intent = new Intent(this , ComplexRecycleView.class);
			startActivity(intent);
			break;
		  }
		case R.id.id_action_refesh:{
			Intent intent = new Intent(this , RefreshActivity.class);
			startActivity(intent);
			break;
		}
			
		}
		return true;
	}

}
