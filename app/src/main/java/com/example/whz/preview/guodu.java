package com.example.whz.preview;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import android.opengl.GLES20;
public class guodu
{
    int mProgram;//自定义渲染管线着色器程序id
    int muMVPMatrixHandle;//总变换矩阵引用
    int maPositionHandle; //顶点位置属性引用
    int maTexCoorHandle; //顶点纹理坐标属性引用
    int musTexture1Handle;//纹理属性引用
    int musTexture2Handle;//纹理属性引用
    int muTHandle;//比例
    int mProgressHandle;
    int mResolutionHandle;
    String mVertexShader;//顶点着色器
    String mFragmentShader;//片元着色器

    private FloatBuffer   mVertexBuffer;//顶点坐标数据缓冲
    private FloatBuffer   resolution;//顶点坐标数据缓冲
    FloatBuffer   mTexCoorBuffer;//顶点纹理坐标数据缓冲
    int vCount;//顶点数量

    int func=0;

    float width;
    float height;

    float sEnd;//按钮右下角的s、t值
    float tEnd;
    float span;
    public guodu(MySurfaceView mv,
                 float width,float height,	//矩形的宽高
                 float sEnd, float tEnd,float span,int func//右下角的s、t值
    )
    {

        this.width=width;
        this.height=height;
        this.sEnd = sEnd;
        this.tEnd = tEnd;
        this.span=span;
        this.func=func;
        initVertexData();
        initShader(mv);

    }
    //初始化顶点坐标与着色数据的方法
    public void initVertexData()
    {
        //顶点坐标数据的初始化================begin============================
        vCount=6;//每个格子两个三角形，每个三角形3个顶点
        final float UNIT_SIZE=0.0165f;
        float vertices[]=
                {
                        -width*UNIT_SIZE, height*UNIT_SIZE, 0,
                        -width*UNIT_SIZE, -height*UNIT_SIZE, 0,
                        width*UNIT_SIZE, height*UNIT_SIZE, 0,

                        -width*UNIT_SIZE, -height*UNIT_SIZE, 0,
                        width*UNIT_SIZE, -height*UNIT_SIZE, 0,
                        width*UNIT_SIZE, height*UNIT_SIZE, 0,
                };
        //创建顶点坐标数据缓冲
        //vertices.length*4是因为一个整数四个字节
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());//设置字节顺序
        mVertexBuffer = vbb.asFloatBuffer();//转换为int型缓冲
        mVertexBuffer.put(vertices);//向缓冲区中放入顶点坐标数据
        mVertexBuffer.position(0);//设置缓冲区起始位置

        //顶点纹理坐标数据的初始化================begin============================
        float texCoor[]=new float[]//纹理坐标
                {
                        0+span,0, 0+span,tEnd, sEnd+span,0,
                        0+span,tEnd, sEnd+span,tEnd, sEnd+span,0
                };
        //创建顶点纹理坐标数据缓冲
        ByteBuffer cbb = ByteBuffer.allocateDirect(texCoor.length*4);
        cbb.order(ByteOrder.nativeOrder());//设置字节顺序
        mTexCoorBuffer = cbb.asFloatBuffer();//转换为Float型缓冲
        mTexCoorBuffer.put(texCoor);//向缓冲区中放入顶点着色数据
        mTexCoorBuffer.position(0);//设置缓冲区起始位置
        //特别提示：由于不同平台字节顺序不同数据单元不是字节的一定要经过ByteBuffer
        //转换，关键是要通过ByteOrder设置nativeOrder()，否则有可能会出问题
        //顶点纹理坐标数据的初始化================end============================

        float wh[]=
                {
                        MainActivity.screenWidth,MainActivity.screenHeight

                };
        //创建顶点坐标数据缓冲
        //vertices.length*4是因为一个整数四个字节
        ByteBuffer abb = ByteBuffer.allocateDirect(wh.length*4);
        abb.order(ByteOrder.nativeOrder());//设置字节顺序
        resolution = abb.asFloatBuffer();//转换为int型缓冲
        resolution.put(wh);//向缓冲区中放入顶点坐标数据
        resolution.position(0);//设置缓冲区起始位置
    }
    public void initShader(MySurfaceView mv)
    {
        //加载顶点着色器的脚本内容
        mVertexShader=ShaderUtil.loadFromAssetsFile("vertex_tex.sh", mv.getResources());
        //加载片元着色器的脚本内容
        if(func==0){
            mFragmentShader=ShaderUtil.loadFromAssetsFile("cube.sh", mv.getResources());

        }
        else if(func==1){
            mVertexShader=ShaderUtil.loadFromAssetsFile("vertex_rotate_scale.sh", mv.getResources());
            mFragmentShader=ShaderUtil.loadFromAssetsFile("rotate_scale.sh", mv.getResources());
        }
        else if(func==2){
            mFragmentShader=ShaderUtil.loadFromAssetsFile("fade.sh", mv.getResources());
        }
        else if(func==3){
            mFragmentShader=ShaderUtil.loadFromAssetsFile("cross_hatch.sh", mv.getResources());
        }
        else if(func==4){
            mFragmentShader=ShaderUtil.loadFromAssetsFile("cross_zoom.sh", mv.getResources());
        }
        else if(func==5){
            mVertexShader=ShaderUtil.loadFromAssetsFile("vertex_rotate_scale.sh", mv.getResources());
            mFragmentShader=ShaderUtil.loadFromAssetsFile("scale_rotate.sh", mv.getResources());
        }
        else if(func==6){
            mFragmentShader=ShaderUtil.loadFromAssetsFile("doom_screen_transition.sh", mv.getResources());
        }
        else if(func==7){
            mFragmentShader=ShaderUtil.loadFromAssetsFile("door_way.sh", mv.getResources());
        }
        else if(func==8){
            mFragmentShader=ShaderUtil.loadFromAssetsFile("morph.sh", mv.getResources());
        }
        else if(func==9){
            mFragmentShader=ShaderUtil.loadFromAssetsFile("mosaic.sh", mv.getResources());
        }
        else if(func==10){
            mFragmentShader=ShaderUtil.loadFromAssetsFile("page_curl.sh", mv.getResources());
        }
        else if(func==11){
            mFragmentShader=ShaderUtil.loadFromAssetsFile("star_swip.sh", mv.getResources());
        }
        else if(func==12){
            mFragmentShader=ShaderUtil.loadFromAssetsFile("swap.sh", mv.getResources());
        }
        else if(func==13){
            mFragmentShader=ShaderUtil.loadFromAssetsFile("undulating_burn_out.sh", mv.getResources());
        }
        //基于顶点着色器与片元着色器创建程序
        mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);
        //获取程序中顶点位置属性引用
        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        //获取程序中总变换矩阵id
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        //获取程序中顶点纹理坐标属性引用
        maTexCoorHandle= GLES20.glGetAttribLocation(mProgram, "aTexCoor");
        //获取纹理属性引用
        musTexture1Handle=GLES20.glGetUniformLocation(mProgram, "from");
        musTexture2Handle=GLES20.glGetUniformLocation(mProgram, "to");
        mResolutionHandle = GLES20.glGetUniformLocation(mProgram, "resolution");
        mProgressHandle=GLES20.glGetUniformLocation(mProgram, "progress");
    }
    public void drawSelf(int texId1, int texId2, float t)
    {
        //制定使用某套着色器程序
        GLES20.glUseProgram(mProgram);
        //将最终变换矩阵传入着色器程序
        GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);
        //将比例传入着色器程序
        GLES20.glUniform1f(mProgressHandle, t);
        GLES20.glUniform2fv(mResolutionHandle, 1, resolution);
        // 将顶点纹理坐标数据传入渲染管线
        GLES20.glVertexAttribPointer
                (
                        maTexCoorHandle,
                        2,
                        GLES20.GL_FLOAT,
                        false,
                        2 * 4,
                        mTexCoorBuffer
                );
        // 启用顶点位置数据
        GLES20.glEnableVertexAttribArray(maPositionHandle);
        GLES20.glEnableVertexAttribArray(maTexCoorHandle);
        //绑定纹理
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texId1);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texId2);

        GLES20.glUniform1i(musTexture1Handle, 0);
        GLES20.glUniform1i(musTexture2Handle, 1);
        //将顶点法向量数据传入渲染管线
        GLES20.glVertexAttribPointer
                (
                        maPositionHandle,
                        3,
                        GLES20.GL_FLOAT,
                        false,
                        3*4,
                        mVertexBuffer
                );
        //绘制矩形
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount);
    }



}
