import "@mantine/core/styles.css";

import { Box, Flex, NavLink } from "@mantine/core";
import { FC } from "react";
import { Logo } from "src/components/atoms/Logo";

export const IssuingHeader: FC = () => {
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
        <NavLink label="請求書発行" h="100%" href={"/issuing/issue"} />
        <NavLink label="請求書一覧" h="100%" href={"/issuing/invoices"} />
      </Flex>
      <Box>
        <Flex>
          <Box>
            <NavLink
              label="受領アカウントに切り替え"
              h="100%"
              href={"/recipient"}
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
