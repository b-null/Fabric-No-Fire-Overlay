package vvbj.modding.no_fire_overlay.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.gui.entries.BooleanListEntry;
import me.shedaniel.clothconfig2.gui.entries.EnumListEntry;
import me.shedaniel.clothconfig2.gui.entries.IntegerSliderEntry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
public class ConfigScreen implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {

            ConfigBuilder builder = ConfigBuilder.create().setParentScreen(parent)
                      .setTitle(Text.literal("No Fire Overlay"));

            ModConfig cfg = AutoConfig.getConfigHolder(ModConfig.class).getConfig();

            ConfigEntryBuilder entryBuilder = builder.entryBuilder();

            ConfigCategory general = builder.getOrCreateCategory(Text.literal("General"));

            // Enabled
            BooleanListEntry enabled = entryBuilder.startBooleanToggle(Text.literal("Enabled"), cfg.enabled)
                            .setDefaultValue(true)
                            .setSaveConsumer(value -> cfg.enabled = value)
            .build();

            general.addEntry(enabled);

            EnumListEntry<ModConfig.OverlayMode> mode = entryBuilder.startEnumSelector(Text.literal("Mode"), ModConfig.OverlayMode.class, cfg.mode)
                            .setDefaultValue(ModConfig.OverlayMode.HIDE)
                            .setSaveConsumer(value -> cfg.mode = value)
                            .setDisplayRequirement(enabled::getValue)
            .build();

            general.addEntry(mode);

            BooleanListEntry showFireIcon = entryBuilder.startBooleanToggle(Text.literal("Show fire icon"), cfg.showCrosshairFireIcon)
                            .setDefaultValue(true)
                            .setSaveConsumer(value -> cfg.showCrosshairFireIcon = value)
                            .setDisplayRequirement(() ->enabled.getValue() && mode.getValue() == ModConfig.OverlayMode.HIDE)
            .build();

            general.addEntry(showFireIcon);

            IntegerSliderEntry fireIconSize = entryBuilder.startIntSlider(Text.literal("Fire icon size"), cfg.fireIconSize, 5, 20)
                            .setDefaultValue(10)
                            .setTextGetter(integer -> Text.literal(Float.toString(integer / 10f)))
                            .setSaveConsumer(value -> cfg.fireIconSize = value)
                            .setDisplayRequirement(() -> enabled.getValue() && mode.getValue() == ModConfig.OverlayMode.HIDE && showFireIcon.getValue())
            .build();

            general.addEntry(fireIconSize);

            builder.setSavingRunnable(() -> AutoConfig.getConfigHolder(ModConfig.class).save());
            return builder.build();
        };
    }
}
