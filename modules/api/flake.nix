{
  description = "MoreCat API Dev";

  inputs = {
    nixpkgs.url = "https://flakehub.com/f/NixOS/nixpkgs/0";
  };

  outputs = { self, nixpkgs }:
    let
      overlays = [
        (final: prev:
          let jdk = prev.openjdk21; in
          {
            jre = jdk;
            inherit jdk;
          })
      ];
      allSystems = [
        "x86_64-linux"
        "aarch64-linux"
        "x86_64-darwin"
        "aarch64-darwin"
      ];
      # Helper to provide system-specific attributes
      forAllSystems = f: nixpkgs.lib.genAttrs allSystems (system: f {
        pkgs = import nixpkgs { inherit overlays system; };
      });
    in
    {
      devShells = forAllSystems ({ pkgs }: {
        default = pkgs.mkShell {
          packages = with pkgs; [
            sbt
          ];
        };
      });
    };
}
