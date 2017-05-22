precision mediump float;
varying vec2 vTextureCoord;//接收从顶点着色器过来的参数
uniform sampler2D sTexture1;//纹理内容数据
uniform float progress;
void main() {           
    gl_FragColor = texture2D(sTexture1, vTextureCoord); 
	gl_FragColor.a=progress;
}              