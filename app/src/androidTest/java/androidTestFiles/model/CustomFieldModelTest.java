package androidTestFiles.model;

import static androidTestFiles.utils.parent.BaseTest.PATH_CUSTOM_FIELDS_TESTS;

import android.Manifest;
import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.digitalcampus.oppia.model.CustomField;
import org.digitalcampus.oppia.utils.storage.Storage;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import androidTestFiles.utils.FileUtils;
import androidx.test.rule.GrantPermissionRule;

@RunWith(AndroidJUnit4.class)
public class CustomFieldModelTest {
    @Rule
    public GrantPermissionRule mRuntimePermissionRule = GrantPermissionRule.grant(Manifest.permission.WRITE_EXTERNAL_STORAGE);

    private Context context;

    @Before
    public void setUp() throws Exception {
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();

    }

    private String copyFile(String filename){
        String destination = Storage.getStorageLocationRoot(context) + File.separator + "test" + File.separator;
        FileUtils.copyFileFromAssets(context, PATH_CUSTOM_FIELDS_TESTS, filename, new File(destination));
        return destination + filename;
    }

    public String loadJSONFromAsset(Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(CustomField.CUSTOMFIELDS_FILE);

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }
    @Test
    public void loadingTest(){
        String json_file = copyFile(CustomField.CUSTOMFIELDS_FILE);
        String json = loadJSONFromAsset(context);
        CustomField cf = new CustomField();
        cf.loadCustomFields(context, json);
        cf.parseRegisterSteps(json);
    }

}
