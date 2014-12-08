package com.dsitelecom.xmontero.crousepantomime;

import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.peculiargames.andmodplug.MODResourcePlayer;
import com.peculiargames.andmodplug.PlayerThread;

public class MainActivity extends ActionBarActivity
{
	private Button btnStart;
	private Button btnStop;

	private TextView txtName;
	private TextView txtTempo;
	private TextView txtEvents;

	private MODResourcePlayer modPlayer = null;

	private final Handler mainThreadHandler = new Handler();

	@Override
	protected void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );

		setContentView( R.layout.activity_main );
		initLayout();
	}

	@Override
	protected void onDestroy()
	{
		playStop();

		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu( Menu menu )
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate( R.menu.menu_main, menu );
		return true;
	}

	@Override
	public boolean onOptionsItemSelected( MenuItem item )
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if( id == R.id.action_settings )
		{
			return true;
		}

		return super.onOptionsItemSelected( item );
	}

	private synchronized void initLayout()
	{
		btnStart = ( Button ) findViewById( R.id.btnStart );
		btnStop = ( Button ) findViewById( R.id.btnStop );

		txtName = ( TextView ) findViewById( R.id.txtName );
		txtTempo = ( TextView ) findViewById( R.id.txtTempo );
		txtEvents = ( TextView ) findViewById( R.id.txtEvents );

		btnStop.setEnabled( false );

		btnStart.setOnClickListener(
				new View.OnClickListener()
				{
					@Override
					public void onClick( View v )
					{
						v.setEnabled( false );
						btnStop.setEnabled( true );

						playStart();
					}
				}
		);

		btnStop.setOnClickListener(
				new View.OnClickListener()
				{
					@Override
					public void onClick( View v )
					{
						v.setEnabled( false );
						btnStart.setEnabled( true );

						playStop();
					}
				}
		);
	}

	private void playStart()
	{
		playStop();

		modPlayer = new MODResourcePlayer( this );
		modPlayer.setPlayerListener(
				new PlayerThread.PlayerListener()
				{
					@Override
					public void onPlayerEvent( int i )
					{
						final int finalI = i;

						// Send something to the main thread.
						mainThreadHandler.post(
								new Runnable()
								{
									@Override
									public void run()
									{
										txtEvents.setText( txtEvents.getText() + "/" + finalI );
									}
								}
						);
					}
				}
		);

		modPlayer.LoadMODResource( R.raw.crouse_pantomime );
		modPlayer.start();
	}

	private void playStop()
	{
		if( modPlayer != null )
		{
			modPlayer.StopAndClose();
			modPlayer = null;
		}
	}
}
