package activity.app.gxj.com.gridlayout;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by gxj on 2016/8/8.
 */
public class GridLayoutActivity extends AppCompatActivity implements View.OnTouchListener {

    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);

        editText = (EditText) findViewById(R.id.editText);
        editText.setOnTouchListener(this);
    }



    /**
     * 隐藏系统键盘  让输入框 不条用系统键盘
     */
    public void hintSystemSoftKeyboard(){
        if (editText.getWindowToken() != null) {
            ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(editText.getWindowToken(), 2);
        }
        if (Build.VERSION.SDK_INT >= 11) {
            try{
                Class[] arrayOfClass = new Class[1];
                arrayOfClass[0] = Boolean.TYPE;
                Method localMethod = null;
                if (Build.VERSION.SDK_INT < 17){
                    localMethod = EditText.class.getMethod("setSoftInputShownOnFocus", arrayOfClass);
                }else{
                    localMethod = EditText.class.getMethod("setShowSoftInputOnFocus", arrayOfClass);
                }
                localMethod.setAccessible(true);
                Object[] arrayOfObject = new Object[1];
                arrayOfObject[0] = Boolean.valueOf(false);
                localMethod.invoke(this, arrayOfObject);
                return;
            }
            catch (SecurityException localSecurityException) {
                localSecurityException.printStackTrace();
                return;
            }
            catch (NoSuchMethodException localNoSuchMethodException) {
                localNoSuchMethodException.printStackTrace();
                return;
            }
            catch (Exception localException) {
                localException.printStackTrace();
                return;
            }
        }
        editText.setInputType(InputType.TYPE_NULL);
    }


    //第二种方式
    public void hideSoftInputMethod() {
        this.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        int currentVersion = android.os.Build.VERSION.SDK_INT;
        String methodName = null;
        if (currentVersion >= 16) {
            // 4.2
            methodName = "setShowSoftInputOnFocus";
        } else if (currentVersion >= 14) {
            // 4.0
            methodName = "setSoftInputShownOnFocus";
        }
        if (methodName == null) {
            editText.setInputType(InputType.TYPE_NULL);
        } else {
            Class<EditText> cls = EditText.class;
            Method setShowSoftInputOnFocus;
            try {
                setShowSoftInputOnFocus = cls.getMethod(methodName,
                        boolean.class);
                setShowSoftInputOnFocus.setAccessible(true);
                setShowSoftInputOnFocus.invoke(editText, false);
            } catch (NoSuchMethodException e) {
                editText.setInputType(InputType.TYPE_NULL);
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_UP) {
            switch (v.getId()) {
                case R.id.editText:
                    editText.requestFocus();
                    hintSystemSoftKeyboard();
                    break;

                default:
                    break;
            }

            return true;
        }
        return false;
    }
}
