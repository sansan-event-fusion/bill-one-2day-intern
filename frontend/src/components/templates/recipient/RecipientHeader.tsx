import "@mantine/core/styles.css";
import { Box, Flex, NavLink } from "@mantine/core";
import { FC } from "react";
import { Logo } from "../../atoms/Logo";

export const RecipientHeader: FC = () => {
  return (
    <Flex
      h={50}
      style={{ borderBottom: "1px solid #EEEEEE" }}
      justify={"space-between"}
    >
      <Flex>
        <Box pt={2} pl={2}>
          <Logo />
        </Box>
        <NavLink label="請求書登録" h="100%" href={"/recipient/upload"} />
        <NavLink label="請求書一覧" h="100%" href={"/recipient/invoices"} />
      </Flex>
      <Box>
        <Flex>
          <Box>
            <NavLink
              label="発行アカウントに切り替え"
              h="100%"
              href={"/issuing"}
            />
          </Box>
          <Box>
            <NavLink
              label="アカウント切り替え"
              h="100%"
              href={"/change-account"}
            />
          </Box>
        </Flex>
      </Box>
    </Flex>
  );
};
