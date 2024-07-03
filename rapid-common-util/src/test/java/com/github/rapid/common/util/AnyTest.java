package com.github.rapid.common.util;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

public class AnyTest {

	ObjectMapper om = new ObjectMapper();
	
	@Test
	public void test() throws Exception, JsonMappingException, IOException {
		
		System.out.println(om.readValue("[{\"role\":\"user\",\"content\":\"# 角色\\n你是一位风趣且花心思的场景信息提取大师，专门从小说中勾勒并提炼出的场景信息。\\n你会：\\n1. 详尽而细心地慢慢一行一行阅读用户提供的小说文本；\\n2. 尽可能多地准确识别出小说中的每个环境场景，且环境场景不可重复；\\n3. scene_list一共包含7个关键结果，分别是en_scene_name、ch_scene_name、en_indoor_or_outdoor、en_main_building_with_color、en_daytime_or_night、en_main_item_with_color和en_scene_prompt，每个关键结果都要坚定不移地输出完整，尤其是en_scene_prompt一定要输出完整；\\n\\n## 关键结果\\n1. 属性scene_num的属性值是你在小说中提取到的环境场景数量，注意要尽可能多地去识别出环境场景，环境场景一定要在5-9个。\\n2. 属性ch_scene_name的属性值是场景中文名称，必须使用中文喔，这个属性值不可以为空；\\n3. 属性en_scene_name的属性值是scene name in english，必须使用英文喔，这个属性值不可以为空，也禁止填unknown；\\n4. 属性en_indoor_or_outdoor的属性值是One word can only be indoor or outdoor，只能是indoor或者outdoor，这个属性值不可以为空，也禁止填unknown；\\n5. 属性en_main_building_with_color的属性值是A phrase describing a major building with colors，这个属性值不可以为空，也禁止填unknown；例如：black temples,red rooms,brown schools,green classrooms,blue offices,dark green clock towers,grey towers\\n6. 属性en_daytime_or_night的属性值是One word can only be daytime or night，只能是daytime或者night，这个属性值不可以为空，也禁止填unknown；\\n7.属性en_main_item_with_color的属性值A phrase describing a major item with colors，这个属性值不可以为空，也禁止填unknown\\n- if en_indoor_or_outdoor == 'indoor' then main item in (bed,bookcase,desk,Bookshelves, sofas, televisions, fireplaces, long tables, curtains, chandeliers, hanging paintings, carpets, vases, chairs, tableware, appliances, home decor, shoe racks, storage cabinets)\\n- if en_indoor_or_outdoor == 'outdoor' then main item in (fountain,Ferris wheel，arbor,lawn,Park benches, basketball courts, amusement parks, gardens, outdoor stages, children's playground facilities, garden landscapes, lakes, rivers)\\nend output:a word that describe colors + main item\\n8. 属性en_scene_prompt的属性值是English descriptive words for the scene，这个属性值不可以为空，也禁止填unknown，注意这个en_scene_prompt属性值千万要输出完整\\n- output:en_main_building_with_color,en_daytime_or_night,en_main_item_with_color,en_indoor_or_outdoor\\n\\n## 输出格式规范\\n{\\n    \\\\\\\"scene_num\\\\\\\":\\\\\\\"{Number of scenes}\\\\\\\",\\n    \\\\\\\"scene_list\\\\\\\": [\\n        {\\n            \\\\\\\"en_scene_name\\\\\\\":\\\\\\\"{scene name in english1}\\\\\\\",\\n            \\\\\\\"ch_scene_name\\\\\\\":\\\\\\\"{场景中文名称1}\\\\\\\",\\n            \\\\\\\"en_indoor_or_outdoor\\\\\\\":\\\\\\\"{One word can only be indoor or outdoor}\\\\\\\",\\n            \\\\\\\"en_main_building_with_color\\\\\\\":\\\\\\\"{A phrase describing a major building with colors}\\\\\\\",\\n            \\\\\\\"en_daytime_or_night\\\\\\\":\\\\\\\"{One word can only be daytime or night}\\\\\\\",\\n            \\\\\\\"en_main_item_with_color\\\\\\\":\\\\\\\"{A phrase describing a major item with colors}\\\\\\\",\\n            \\\\\\\"en_scene_prompt\\\\\\\":\\\\\\\"{en_main_building_with_color,en_daytime_or_night,en_main_item_with_color,en_indoor_or_outdoor}\\\\\\\"\\n        },\\n        {\\n            \\\\\\\"en_scene_name\\\\\\\":\\\\\\\"{scene name in english2}\\\\\\\",\\n            \\\\\\\"ch_scene_name\\\\\\\":\\\\\\\"{场景中文名称2}\\\\\\\",\\n            \\\\\\\"en_indoor_or_outdoor\\\\\\\":\\\\\\\"{One word can only be indoor or outdoor}\\\\\\\",\\n            \\\\\\\"en_main_building_with_color\\\\\\\":\\\\\\\"{A phrase describing a major building with colors}\\\\\\\",\\n            \\\\\\\"en_daytime_or_night\\\\\\\":\\\\\\\"{One word can only be daytime or night}\\\\\\\",\\n            \\\\\\\"en_main_item_with_color\\\\\\\":\\\\\\\"{A phrase describing a major item with colors}\\\\\\\",\\n            \\\\\\\"en_scene_prompt\\\\\\\":\\\\\\\"{en_main_building_with_color,en_daytime_or_night,en_main_item_with_color,en_indoor_or_outdoor}\\\\\\\"\\n        }\\n    ],\\n    \\\\\\\"scene_total_name\\\\\\\": \\\\\\\"{en_scene_name1}:{ch_scene_name1},{en_scene_name2}:{ch_scene_name2}\\\\\\\"\\n}\\n\\n${story}\"}]", List.class));
		
		System.out.println(System.currentTimeMillis());
		System.out.println(Math.ceil(1.1));
		System.out.println(Math.floor(1.1));
		System.out.println(1 == new Long(1));
		System.out.println("9e0d79aa0901e3694cefb2eef871a49cd9f3de5c3e95e99d060970e04c827097affda6dda5433ca1452da41a932e6fb92d1c1b7c424b69398878e3b170ada7c605401574a4bfd9f24d8db5e61031d584f491250a1bacc62d7e0301cf7affc2754ea1ec66ab9d453bcb45edfee2ce98ec2edfb480c8c3121ee527d32069453d7a".length());
//		String key = "aliyun-qiu-dockerkey-Zdf23XgfgFd";
//		System.out.println(key.length());
		// 1700702889244
		// 1700703088365
//		System.out.println(System.currentTimeMillis());
		Timestamp start = new Timestamp(1703836606004L);
		System.out.println(start);
		Timestamp end = new Timestamp(1703836615531L);
		System.out.println(end);
		System.out.println(end.getTime() - start.getTime());
		
		System.out.println("{\"prompt\":\"A beautiful lotus flower --niji 5 --ar 3:4\",\"sign\":\"1cbd7139343914d4ccb3226002c13a08\"}");
		
		ExecutorService executorService = Executors.newFixedThreadPool(200);
		for(int i = 0; i < 10000; i++) {
			final int finalI = i;
			executorService.submit(() -> {
				ThreadUtil.sleep(1000 * 5);
				System.out.println(finalI);
			});
		}
		
		ThreadUtil.sleepSeconds(100);
	}
}
