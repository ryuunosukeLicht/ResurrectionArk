package com.licht_meilleur.resurrection_ark.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.text.Text;

public class MobListScreen extends Screen {

    private MobListWidget mobList;

    public MobListScreen() {
        super(Text.literal("Mob List"));
    }

    @Override
    protected void init() {
        mobList = new MobListWidget(client, width, height, 30, height - 30, 35);
        addSelectableChild(mobList);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        mobList.render(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
    }

    // ==== Mob List Widget ====

    class MobListWidget extends EntryListWidget<MobListWidget.MobEntry> {

        public MobListWidget(net.minecraft.client.MinecraftClient client,
                             int width, int height,
                             int top, int bottom, int itemHeight) {
            super(client, width, height, top, bottom, itemHeight);

            // 仮のテストエントリ
            for (int i = 0; i < 10; i++) {
                addEntry(new MobEntry("Mob " + i));
            }
        }

        public class MobEntry extends EntryListWidget.Entry<MobEntry> {

            private final String name;

            public MobEntry(String name) {
                this.name = name;
            }

            @Override
            public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight,
                               int mouseX, int mouseY, boolean hovered, float delta) {

                context.drawText(textRenderer, name, x + 10, y + 7, 0xFFFFFF, false);
            }

            @Override
            public boolean mouseClicked(double mouseX, double mouseY, int button) {
                // TODO: mob クリック処理
                return super.mouseClicked(mouseX, mouseY, button);
            }
        }
    }
}