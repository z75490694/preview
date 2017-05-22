package com.example.whz.preview;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import android.opengl.GLSurfaceView;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.os.Environment;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

class MySurfaceView extends GLSurfaceView
{
	private SceneRenderer mRenderer;//场景渲染器
	private String[] path={Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator
			+"DCIM/Camera/lyf1.jpg",Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator
			+"DCIM/Camera/lyf2.jpg",Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator
			+"DCIM/Camera/lyf3.jpg",Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator
			+"DCIM/Camera/lyf4.jpg",Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator
			+"DCIM/Camera/lyf5.jpg",Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator
			+"DCIM/Camera/lyf6.jpg",Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator
			+"DCIM/Camera/lyf7.jpg",Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator
			+"DCIM/Camera/lyf8.jpg",Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator
			+"DCIM/Camera/lyf9.jpg",Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator
			+"DCIM/Camera/lyf10.jpg",Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator
			+"DCIM/Camera/lyf11.jpg",Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator
			+"DCIM/Camera/lyf12.jpg",Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator
			+"DCIM/Camera/lyf13.jpg",Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator
			+"DCIM/Camera/lyf14.jpg",Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator
			+"DCIM/Camera/lyf15.jpg",Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator
			+"DCIM/Camera/lyf16.jpg",Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator
			+"DCIM/Camera/lyf17.jpg",Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator
			+"DCIM/Camera/lyf18.jpg",Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator
			+"DCIM/Camera/lyf19.jpg",Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator
			+"DCIM/Camera/lyf20.jpg",Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator
			+"DCIM/Camera/lyf21.jpg",Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator
			+"DCIM/Camera/lyf22.jpg",Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator
			+"DCIM/Camera/lyf23.jpg",Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator
			+"DCIM/Camera/lyf24.jpg",Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator
			+"DCIM/Camera/lyf25.jpg",Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator
			+"DCIM/Camera/lyf26.jpg",Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator
			+"DCIM/Camera/lyf27.jpg",Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator
			+"DCIM/Camera/lyf28.jpg",Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator
			+"DCIM/Camera/lyf29.jpg",Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator
			+"DCIM/Camera/lyf30.jpg",Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator
			+"DCIM/Camera/lyf31.jpg",Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator
			+"DCIM/Camera/lyf32.jpg"};
	private int pathLength=path.length;
	private int textureId[]=new int[pathLength];
	private int[] ih=new int[pathLength];
	private int[] iw=new int[pathLength];
	private float[] r=new float[pathLength];
	private int imageHeight;
	private int imageWidth;
	private int width=MainActivity.screenWidth;
	private int height=MainActivity.screenHeight;

	public MySurfaceView(Context context) {
		super(context);
		this.setEGLContextClientVersion(2); //设置使用OPENGL ES2.0
		mRenderer = new SceneRenderer();	//创建场景渲染器
		setRenderer(mRenderer);				//设置渲染器
		setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);//设置渲染模式为主动渲染

	}
	float t = 0;
	float span =0;
	int flip=0;
	private class SceneRenderer implements GLSurfaceView.Renderer
	{
		TextureRect pmBase;
		TextureRect2 pmBase2;
		TextureRect3 pmBase3;
		TextureRect4 pmBase4;
		guodu mguodu;
		int texId3;
		int texId4;
		private int num=0;
		private int[] fun=new int[pathLength];
		private int[] gfun=new int[pathLength];
		private boolean flag=true;
		private boolean count=false;
		private float alpha=0;
		private float angel=0;
		private float scale=1.0f;
		int frameBufferId;
		int shadowId;// 动态产生的阴影纹理Id
		int renderDepthBufferId;// 动态产生的阴影纹理Id
		boolean isBegin=true;
		boolean rotate=true;
		public void initFRBuffers()
		{
			int[] tia=new int[1];
			GLES20.glGenFramebuffers(1, tia, 0);
			frameBufferId=tia[0];

			if(isBegin)
			{
				GLES20.glGenRenderbuffers(1, tia, 0);
				renderDepthBufferId=tia[0];
				GLES20.glBindRenderbuffer(GLES20.GL_RENDERBUFFER, renderDepthBufferId);
				GLES20.glRenderbufferStorage(GLES20.GL_RENDERBUFFER, GLES20.GL_DEPTH_COMPONENT16, width, height);
				isBegin=false;
			}

		}
		//通过绘制产生阴影纹理
		public void generateShadowImage()
		{
			initFRBuffers();

			GLES20.glViewport(0, 0, width, height);
			GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, frameBufferId);
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, shadowId);
			GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_LINEAR);
			GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_LINEAR);
			GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,GLES20.GL_CLAMP_TO_EDGE);
			GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_CLAMP_TO_EDGE);

			GLES20.glFramebufferTexture2D
					(
							GLES20.GL_FRAMEBUFFER,
							GLES20.GL_COLOR_ATTACHMENT0,
							GLES20.GL_TEXTURE_2D,
							shadowId,
							0
					);

			GLES20.glTexImage2D
					(
							GLES20.GL_TEXTURE_2D,
							0,
							GLES20.GL_RGB,
							width,
							height,
							0,
							GLES20.GL_RGB,
							GLES20.GL_UNSIGNED_SHORT_5_6_5,
							null
					);

			GLES20.glFramebufferRenderbuffer
					(
							GLES20.GL_FRAMEBUFFER,
							GLES20.GL_DEPTH_ATTACHMENT,
							GLES20.GL_RENDERBUFFER,
							renderDepthBufferId
					);
			GLES20.glClear( GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
		}

		public void onDrawFrame(GL10 gl)
		{
			//清除深度缓冲与颜色缓冲
			//GLES20.glClear( GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
			// MatrixState.pushMatrix();
			//s GLES20.glClear( GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
			if(flag){

				if(num<pathLength){
					if(fun[num]==0){
						if(count){

							generateShadowImage();
							GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, frameBufferId);
							shadowId=textureId[num];
							pmBase= new TextureRect(MySurfaceView.this,(float)width/2,(float)height/2, r[num],1f,span);
							pmBase.drawSelf(shadowId,alpha);
							//if(alpha<0.99){
							// 	 alpha=alpha+0.01f;
							//  }
							GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texId4);

							GLES20.glCopyTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGB, 0, 0,width, height, 0);
							GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
									GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
							GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
									GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);

							//  GLES20.glDeleteFramebuffers(1, new int[]{frameBufferId}, 0);
							//  GLES20.glDeleteTextures(1, new int[]{shadowId}, 0);
							flag=false;
						}
						else{
							GLES20.glClear( GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
							pmBase = new TextureRect(MySurfaceView.this,(float)width/2,(float)height/2, r[num],1f,span);
							shadowId=textureId[num];
							pmBase.drawSelf(shadowId,alpha);
							if(alpha<0.99){
								alpha=alpha+0.01f;
							}
							span=span+0.001f;
							if(span+r[num]>1){
								span=0;
								num=num+1;
								texId3=copyScreenToTexture(width,height);
								//fun[num]=(func+1)%4;
								count=true;
							}
							if(num==pathLength){
								texId3=copyScreenToTexture(width,height);
							}
						}

					}

					else if(fun[num]==1){

						if(count){

							generateShadowImage();
							GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, frameBufferId);
							shadowId=textureId[num];
							pmBase2= new TextureRect2(MySurfaceView.this,(float)width/2,(float)height/2, r[num],1f,span);
							pmBase2.drawSelf(shadowId,alpha);
							GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texId4);

							GLES20.glCopyTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGB, 0, 0,width, height, 0);
							GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
									GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
							GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
									GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);

							//  GLES20.glDeleteFramebuffers(1, new int[]{frameBufferId}, 0);
							//  GLES20.glDeleteTextures(1, new int[]{shadowId}, 0);
							flag=false;
						}
						else{
							GLES20.glClear( GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
							pmBase2 = new TextureRect2(MySurfaceView.this,(float)width/2,(float)height/2, r[num],1f,span);
							shadowId=textureId[num];
							pmBase2.drawSelf(shadowId,alpha);
							if(alpha<0.99){
								alpha=alpha+0.01f;
							}
							span=span+0.001f;
							if(span+r[num]>1){
								span=0;
								num=num+1;
								texId3=copyScreenToTexture(width,height);
								//func=(func+1)%4;
								count=true;
							}
							if(num==pathLength){
								texId3=copyScreenToTexture(width,height);
							}
						}
					}
					else if(fun[num]==2){
						if(count){

							generateShadowImage();
							GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, frameBufferId);
							shadowId=textureId[num];
							pmBase3=new TextureRect3(MySurfaceView.this,(float)width/2,(float)ih[num]*width/iw[num]/2, 1f,1f,span);
							pmBase3.drawSelf(shadowId,alpha);
							GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texId4);

							GLES20.glCopyTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGB, 0, 0,width, height, 0);
							GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
									GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
							GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
									GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);

							//  GLES20.glDeleteFramebuffers(1, new int[]{frameBufferId}, 0);
							//  GLES20.glDeleteTextures(1, new int[]{shadowId}, 0);
							flag=false;
						}
						else{
							GLES20.glClear( GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
							pmBase3 = new TextureRect3(MySurfaceView.this,(float)width/2,(float)ih[num]*width/iw[num]/2, 1f,1f,span);
							shadowId=textureId[num];
							if(alpha<0.99){
								alpha=alpha+0.01f;
							}
							pmBase3.drawSelf(shadowId,alpha);
							span=span+0.0004f;
							if(span>0.1){
								span=0;
								num=num+1;
								texId3=copyScreenToTexture(width,height);
								//func=(func+1)%4;
								count=true;
							}
							if(num==pathLength){
								texId3=copyScreenToTexture(width,height);
							}
						}
					}
					else if(fun[num]==3){
						if(count){

							generateShadowImage();
							GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, frameBufferId);
							shadowId=textureId[num];

							pmBase4=new TextureRect4(MySurfaceView.this,(float)width/2,(float)ih[num]*width/iw[num]/2, 1f,1f,span);
							pmBase4.drawSelf(shadowId,alpha);
							GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texId4);

							GLES20.glCopyTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGB, 0, 0,width, height, 0);
							GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
									GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
							GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
									GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);

							flag=false;
						}
						else{
							GLES20.glClear( GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
							pmBase4 = new TextureRect4(MySurfaceView.this,(float)width/2,(float)ih[num]*width/iw[num]/2, 1f,1f,span);
							shadowId=textureId[num];
							if(alpha<0.99){
								alpha=alpha+0.01f;
							}
							pmBase4.drawSelf(shadowId,alpha);
							span=span+0.0004f;
							if(span>0.1){
								span=0;
								num=num+1;
								texId3=copyScreenToTexture(width,height);
								//func=(func+1)%4;
								count=true;
							}
							if(num==pathLength){
								texId3=copyScreenToTexture(width,height);
							}
						}
					}

				}
				else{
					MainActivity.mp.release();
					flag=true;
					count=false;
					GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
					pmBase = new TextureRect(MySurfaceView.this,(float)width/2*0.95f,(float)height/2*0.95f,1f,-1f,span);
					shadowId=texId3;
					pmBase.drawSelf(shadowId,1.0f);

				}
			}



			else
			{
				GLES20.glClear( GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

				GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER,0);
				mguodu = new guodu(MySurfaceView.this,(float)width/2, (float)height/2, 1f,1f,span,gfun[num]);
				mguodu.drawSelf(texId3, texId4, t);

				MatrixState.setInitStack();
				if(gfun[num]==0){

					t=t+0.04f;
				}
				else if(gfun[num]==1){
					if(rotate){
						MatrixState.rotate(angel, 0, 0, 1);
						MatrixState.scale(scale, scale, scale);
						angel=angel+20;
						scale=scale-0.04f;
						if(scale<0.01){
							rotate=false;
							MatrixState.setInitStack();
							t=0.002f;
						}

					}
					else{
						t=t+0.02f;
					}



				}
				else if(gfun[num]==2){
					t=t+0.01f;
				}
				else if(gfun[num]==3){
					t=t+0.01f;
				}
				else if(gfun[num]==4){
					t=t+0.2f;
				}
				else if(gfun[num]==5){
					if(rotate){

						t=t+0.02f;

						if(t>0.99)
						{
							rotate=false;
							t=1f;
							scale=0;
							MatrixState.rotate(angel, 0, 0, 1);
							MatrixState.scale(scale, scale, scale);
						}


					}
					else{

						angel=angel+20;
						scale=scale+0.04f;
						MatrixState.rotate(angel, 0, 0, 1);
						MatrixState.scale(scale, scale, scale);
						if(scale>1){
							t=1.1f;
							scale=1f;
							rotate=true;
						}
					}

				}
				else if(gfun[num]==6){
					t=t+0.025f;
				}
				else if(gfun[num]==7){
					t=t+0.025f;
				}
				else if(gfun[num]==8){
					t=t+0.03f;
				}
				else if(gfun[num]==9){
					t=t+0.03f;
				}
				else if(gfun[num]==10){
					t=t+0.18f;
				}
				else if(gfun[num]==11){
					t=t+0.08f;
				}
				else if(gfun[num]==12){
					t=t+0.05f;
				}
				else if(gfun[num]==13){
					t=t+0.07f;
				}


				if(t>1){
					flag=true;
					count=false;
					t=0;
					span=0;
					//gfun=(gfun+1)%14;
					MatrixState.setInitStack();
					angel=0;
					scale=1f;
					rotate=true;
				}
			}

			//  requestRender();

			// MatrixState.popMatrix();


		}

		public void onSurfaceChanged(GL10 gl, int width, int height) {
			//设置视窗大小及位置
			GLES20.glViewport(0, 0, width, height);
			//计算GLSurfaceView的宽高比
			float ratio = (float) width / height;
			//设置camera位置
			MatrixState.setCamera
					(
							0,	//人眼位置的X
							0, //人眼位置的Y
							20, //人眼位置的Z
							0, 	//人眼球看的点X
							0,  //人眼球看的点Y
							0,  //人眼球看的点Z
							0, 	//up向量
							1,
							0
					);
			//调用此方法计算产生透视投影矩阵
			MatrixState.setProjectFrustum(-ratio, ratio, -1, 1, 2, 100);


		}
		@Override
		public void onSurfaceCreated(GL10 gl, EGLConfig config) {
			//设置屏幕背景色RGBA
			GLES20.glClearColor(0f,0f,0f,0);
			//打开深度检测
			GLES20.glEnable(GLES20.GL_DEPTH_TEST);

			//打开背面剪裁
			GLES20.glEnable(GLES20.GL_CULL_FACE);
			//初始化变换矩阵
			MatrixState.setInitStack();
			//pmBase = new TextureRect(MySurfaceView.this,(float)width/2, (float)height/2, 0.5625f,1);


			for(int i=0;i<pathLength;i++){
				int[] textures = new int[1];
				GLES20.glGenTextures
						(
								1,          //产生的纹理id的数量
								textures,   //纹理id的数组
								0           //偏移量
						);

				Bitmap bitmap = scaleBitmap(path[i],MainActivity.screenWidth,MainActivity.screenHeight);
				textureId[i] = textures[0];
				ih[i]=imageHeight;
				iw[i]=imageWidth;
				r[i]=(float)ih[i]/(float)height*(float)width/(float)iw[i];

				GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId[i]);
				GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
				GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
						GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);

				bitmap.recycle();
			}


			MainActivity.mp.start();
			MainActivity.mp.setLooping(true);
			for(int i=0;i<pathLength;i++){
				int rd=Math.random()>0.5?1:0;
				if(ih[i]<400||iw[i]<200){
					fun[i]=rd+2;
				}
				//	else if((float)ih[i]/height>=(float)iw[i]/width){
				//		fun[i]=rd+2;
				//	}
				//	else if((float)ih[i]>(float)height*2/3||((float)iw[i]>(float)ih[i]*2/3)&&(ih[i]>height/2)){
				//		fun[i]=rd;

				//	}
				else if(iw[i]>ih[i]){
					fun[i]=rd;
				}
				else{
					fun[i]=rd+2;
				}
				if(i<pathLength-1){
					if(Math.abs(ih[i]-ih[i+1])<50){
						gfun[i]=(int) Math.round(Math.random()*13);
					}
					else{
						gfun[i]=(int) Math.round(Math.random()*12)+1;
					}
				}
				//gfun[i]=13;
			}
			GLES20.glEnable(GLES20.GL_BLEND);
			GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA,GLES20.GL_ONE_MINUS_SRC_ALPHA);

		}
	}

	//初始化纹理
	public int initTexture(int drawableId)
	{
		//生成纹理ID
		int[] textures = new int[1];
		GLES20.glGenTextures
				(
						1,          //产生的纹理id的数量
						textures,   //纹理id的数组
						0           //偏移量
				);
		int textureId=textures[0];
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
		//非Mipmap纹理采样过滤参数
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_NEAREST);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_LINEAR);

		//ST方向纹理拉伸方式
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,GLES20.GL_REPEAT);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_REPEAT);

		//通过输入流加载图片===============begin===================
		InputStream is = this.getResources().openRawResource(drawableId);
		Bitmap bitmapTmp;
		try
		{
			bitmapTmp = BitmapFactory.decodeStream(is);
		}
		finally
		{
			try
			{
				is.close();
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
		//实际加载纹理,换成这个方法后，如果图片格式有问题，会抛出图片格式异常，不再会误显示其他异常
		GLUtils.texImage2D
				(
						GLES20.GL_TEXTURE_2D, //纹理类型
						0,
						GLUtils.getInternalFormat(bitmapTmp),
						bitmapTmp, //纹理图像
						GLUtils.getType(bitmapTmp),
						0 //纹理边框尺寸
				);
		//自动生成Mipmap纹理
		GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
		//释放纹理图
		bitmapTmp.recycle();
		//返回纹理ID
		return textureId;
	}

	public static int copyScreenToTexture( int p_sizeX, int p_sizeY) {

		int[] textures = new int[1];
		GLES20.glGenTextures
				(
						1,          //产生的纹理id的数量
						textures,   //纹理id的数组
						0           //偏移量
				);
		int textureId=textures[0];

		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
		System.out.println("trying to bind framebuffer");
		//((GLES20ExtensionPack)GLES20).glBindFramebufferOES();
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);

		GLES20.glCopyTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGB, 0, 0, p_sizeX, p_sizeY, 0);
		return textureId;
	}

	public  Bitmap scaleBitmap(String path, int w, int h) {
		Bitmap bit = null;
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		bit = BitmapFactory.decodeFile(path, opts);
		Log.i("asdasd", "opts.outHeight=" + opts.outHeight + "opts.outWidth="
				+ opts.outWidth);
		imageHeight=opts.outHeight;
		imageWidth=opts.outWidth;
		int heightRatio = Math.round((float) imageHeight / (float) h);
		int widthRatio = Math.round((float) imageWidth / (float) w);
		opts.inSampleSize = heightRatio < widthRatio ? widthRatio : heightRatio;
		Log.i("asdasd", "imageHeight " + imageHeight);
		Log.i("asdasd", "imageWidth " + imageWidth);
		Log.i("asdasd", "picture inSampleSize " + opts.inSampleSize);
		if(opts.inSampleSize!=0){
			imageHeight=imageHeight/opts.inSampleSize;
			imageWidth=imageWidth/opts.inSampleSize;
		}
		opts.inJustDecodeBounds = false;
		try {
			bit = BitmapFactory.decodeFile(path, opts);
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		}
		return bit;
	}

	float anis_calc_interpolator(int anis_type,
								 int diff_time, int duration)
	{
		float input = (float)diff_time/duration;

		float res;
		if(anis_type == 0)
		{
			res = input * input;
		}
		else if(anis_type == 1)
		{
			res = (float) (1.0 - (1.0f - input) * (1.0f - input));
		}
		else if(anis_type == 2)
		{
			if (input > 0.5)
			{
				input = (input - 0.5f) / 0.5f;
				res = (1.0f - (1.0f - input) * (1.0f - input)) / 2 + 0.5f;
			}
			else
			{
				input = input / 0.5f;
				res = input * input / 2;
			}
		}
		else// linear
		{
			res = input;
		}
		return res;
	}

}
