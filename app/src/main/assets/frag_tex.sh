precision mediump float;
varying vec2 vTextureCoord;//���մӶ�����ɫ�������Ĳ���
uniform sampler2D sTexture1;//������������
uniform float progress;
void main() {           
    gl_FragColor = texture2D(sTexture1, vTextureCoord); 
	gl_FragColor.a=progress;
}              