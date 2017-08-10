package ty.youngstudy.com.mvp;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by edz on 2017/8/8.
 */

@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface RequirePresenter {
    Class<? extends Presenter> value();
}
