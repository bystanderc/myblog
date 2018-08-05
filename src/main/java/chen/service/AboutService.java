package chen.service;

import chen.common.ServerResponse;
import chen.dto.AboutDto;

/**
 * @author bystander
 * @date 2018/8/1
 */
public interface AboutService {

    AboutDto getAbout();

    ServerResponse updateAbout(String content);
}
