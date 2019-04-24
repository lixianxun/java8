import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

import lombok.AllArgsConstructor;
import lombok.Data;

public class TestStreamMap {

	@Data
	@AllArgsConstructor
	private static class SysConfig {
		private long id;
		private String configkey;
		private String value;
		private String category;
	}

	private static List<SysConfig> list = Lists.newArrayList();
	
	public static void main(String[] args) throws Exception {
        list.add(new SysConfig(1, "key1", "v1", "cat1"));
        list.add(new SysConfig(2, "key2", "v2", "cat1"));
        list.add(new SysConfig(3, "key11", "v11", "cat2"));
        list.add(new SysConfig(4, "key22", "v22", "cat2"));
        
        List<String> ss = list.stream().map(x -> x.getCategory()).distinct().collect(Collectors.toList());
      
        //cat1
        //cat2
        ss.forEach(System.err::println);
        
        //{1=v1, 2=v2, 3=v11, 4=v22}
        Map<Long, String> map = list.stream().collect(Collectors.toMap(SysConfig::getId, SysConfig::getValue));
        System.err.println(map);
        
        //{1=v1, 2=v2, 3=v11, 4=v22}
        map = list.stream().collect(Collectors.toMap(x->x.getId(), x->x.getValue()));
        System.err.println(map);
        
        //{cat2=v22, cat1=v2}
        Map<String, String> map2 = list.stream().collect(
        		Collectors.toMap(x->x.getCategory(), x->x.getValue(), (oldValue, newValue) -> newValue));
        System.err.println(map2);
        
        //{cat1=v2, cat2=v22}
        LinkedHashMap<String, String> map3 = list.stream()
        		.sorted(Comparator.comparingLong(SysConfig::getId).reversed())
        		.collect(
	        		Collectors.toMap(x->x.getCategory(), x->x.getValue()
	        				, (oldValue, newValue) -> newValue
	        				, LinkedHashMap::new));
        System.err.println(map3);
        
        //{cat2=v11, cat1=v1}
        LinkedHashMap<String, String> map4 = list.stream()
        		.sorted(Comparator.comparingLong(SysConfig::getId).reversed())
        		.collect(
        				Collectors.toMap(x->x.getCategory(), x->x.getValue()
        						, (oldValue, newValue) -> newValue
        						, LinkedHashMap::new));
        System.err.println(map4);
        
        //{cat2=[TestStreamMap.SysConfig(id=3, configkey=key11, value=v11, category=cat2), TestStreamMap.SysConfig(id=4, configkey=key22, value=v22, category=cat2)], cat1=[TestStreamMap.SysConfig(id=1, configkey=key1, value=v1, category=cat1), TestStreamMap.SysConfig(id=2, configkey=key2, value=v2, category=cat1)]}
        Map<String, List<SysConfig>> map5 = list.stream()
        		.collect(
        				Collectors.groupingBy(SysConfig::getCategory, Collectors.toList()));
        System.err.println(map5);
	}
}

