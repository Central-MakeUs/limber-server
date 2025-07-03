package club.cmc.limber.common.module.info;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ModuleNameInfo {

    private String name;

    public String toString() {
        return name;
    }

}
